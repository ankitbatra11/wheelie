package com.abatra.android.wheelie.demo;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.print.PrintHelper;

import com.abatra.android.wheelie.activity.ResultContracts.InputLessActivityResultContract;
import com.abatra.android.wheelie.animation.SharedAxisMotion;
import com.abatra.android.wheelie.demo.databinding.ActivityMainBinding;
import com.abatra.android.wheelie.intent.IntentFactory;
import com.abatra.android.wheelie.lifecycle.ILifecycleOwner;
import com.abatra.android.wheelie.media.picker.IntentMediaPicker;
import com.abatra.android.wheelie.media.picker.PickMediaCount;
import com.abatra.android.wheelie.media.picker.PickMediaRequest;
import com.abatra.android.wheelie.media.picker.PickableMediaType;
import com.abatra.android.wheelie.media.printer.ImagePrinter;
import com.abatra.android.wheelie.media.printer.IntentImagePrinter;
import com.abatra.android.wheelie.media.printer.IntentPrintImageRequest;
import com.abatra.android.wheelie.network.InternetConnectionObserver;
import com.abatra.android.wheelie.permission.ManageOverlayPermissionRequestor;
import com.abatra.android.wheelie.permission.ManifestMultiplePermissionsRequestor;
import com.abatra.android.wheelie.permission.ManifestSinglePermissionRequestor;
import com.abatra.android.wheelie.permission.MultiplePermissionsGrantResult;
import com.abatra.android.wheelie.permission.MultiplePermissionsRequestor;
import com.abatra.android.wheelie.update.AppUpdateAvailability;
import com.abatra.android.wheelie.update.AppUpdateAvailabilityChecker;
import com.abatra.android.wheelie.update.AppUpdateAvailabilityCriteria;
import com.abatra.android.wheelie.update.AppUpdateHandler;
import com.abatra.android.wheelie.update.AppUpdateHandlerFactory;
import com.abatra.android.wheelie.update.AppUpdateRequestor;
import com.abatra.android.wheelie.update.playstore.PlayStoreAppUpdateAvailability;
import com.abatra.android.wheelie.update.playstore.PlayStoreAppUpdateRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.snackbar.Snackbar;
import com.google.common.base.Throwables;

import timber.log.Timber;

import static com.abatra.android.wheelie.activity.ResultContracts.AttachData;
import static com.abatra.android.wheelie.activity.ResultContracts.GetContent;
import static com.abatra.android.wheelie.activity.ResultContracts.MediaInfo;
import static com.abatra.android.wheelie.activity.ResultContracts.OpenMedia;
import static com.abatra.android.wheelie.activity.ResultContracts.OpenSettingsScreen.wirelessSettings;
import static com.abatra.android.wheelie.activity.ResultContracts.ShareMedia;
import static com.abatra.android.wheelie.update.AppUpdateType.FLEXIBLE;
import static com.abatra.android.wheelie.update.AppUpdateType.IMMEDIATE;

public class MainActivity extends AppCompatActivity implements ILifecycleOwner {

    private static final String PRINT_JOB_NAME = "print picked image";

    private final IntentMediaPicker intentMediaPicker = new IntentMediaPicker();
    private final InternetConnectionObserver internetConnectionObserver = InternetConnectionObserver.newInstance(this);
    private ActivityResultLauncher<MediaInfo> attachDataLauncher;
    private ActivityResultLauncher<MediaInfo> openMediaLauncher;
    private ActivityResultLauncher<MediaInfo> shareMediaLauncher;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            requestWindowFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
            SharedAxisMotion.X.setExitAnimation(this);
        }

        super.onCreate(savedInstanceState);

        attachDataLauncher = registerForActivityResult(new AttachData(),
                result -> showSnackbarMessage("Attach data result callback"));

        openMediaLauncher = registerForActivityResult(new OpenMedia(),
                result -> showSnackbarMessage("open media result callback"));

        shareMediaLauncher = registerForActivityResult(new ShareMedia(),
                result -> showSnackbarMessage("share media result callback"));

        binding = ActivityMainBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        intentMediaPicker.observeLifecycle(this);
        binding.pickImage.setOnClickListener(v -> {

            PickMediaRequest pickMediaRequest = PickMediaRequest.builder()
                    .pick(PickMediaCount.MULTIPLE)
                    .ofType(PickableMediaType.IMAGE)
                    .withMultipleMediaResultCallback(result -> Timber.d("result=%s", result))
                    .build();

            intentMediaPicker.pickMedia(pickMediaRequest);
        });
        binding.launchEnterAnimatedScreen.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Intent intent = new Intent(v.getContext(), EnterAnimatedActivity.class);
                SharedAxisMotion.X.startActivityWithAnimation(intent, MainActivity.this);
            }
        });

        internetConnectionObserver.observeLifecycle(this);
        binding.checkInternetConnectionBtn.setOnClickListener(v -> {
            LiveData<Boolean> connected = internetConnectionObserver.isConnectedToInternet();
            connected.observe(this, c -> Snackbar.make(v, c.toString(), Snackbar.LENGTH_SHORT).show());
        });

        ActivityResultLauncher<Void> launcher = registerForActivityResult(wirelessSettings(), result -> {
            View view = binding.coordinator;
            Snackbar.make(view, R.string.wireless_settings_result_msg, Snackbar.LENGTH_SHORT).show();
        });
        binding.launchWirelessSettingsBtn.setOnClickListener(v -> launcher.launch(null));

        binding.printImage.setOnClickListener(v -> pickImage(result -> {
            RequestManager requestManager = Glide.with(MainActivity.this);
            requestManager.asBitmap().load(result).into(new CustomTarget<Bitmap>() {

                @Override
                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                    print(resource);
                }

                @Override
                public void onLoadCleared(@Nullable Drawable placeholder) {
                }
            });
        }));
        binding.setImageAsWallpaper.setOnClickListener(v -> pickImage(result -> {
            MediaInfo image = MediaInfo.image(result);
            attachDataLauncher.launch(image);
        }));
        binding.openImage.setOnClickListener(v -> pickImage(result -> {
            MediaInfo image = MediaInfo.image(result);
            openMediaLauncher.launch(image);
        }));
        binding.shareImage.setOnClickListener(v -> pickImage(result -> {
            MediaInfo image = MediaInfo.image(result);
            shareMediaLauncher.launch(image);
        }));

        ManifestSinglePermissionRequestor singlePermissionRequestor = new ManifestSinglePermissionRequestor();

        singlePermissionRequestor.observeLifecycle(this);
        binding.reqCameraPermission.setOnClickListener(v -> {
            String permission = Manifest.permission.CAMERA;
            singlePermissionRequestor.requestSystemPermission(permission, grantResult -> showToastMessage("grantResult=" + grantResult));
        });

        InputLessActivityResultContract contract = new InputLessActivityResultContract(IntentFactory::openAppDetailsSettings);
        ActivityResultLauncher<Void> appDetailsLauncher = registerForActivityResult(contract,
                result -> showToastMessage("app details result=" + result));

        binding.launchAppDetailsSettings.setOnClickListener(v -> appDetailsLauncher.launch(null));

        ManifestMultiplePermissionsRequestor multiplePermissionsRequestor = new ManifestMultiplePermissionsRequestor();
        multiplePermissionsRequestor.observeLifecycle(this);
        binding.reqMultiplePermissions.setOnClickListener(v -> {
            String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO};
            multiplePermissionsRequestor.requestSystemPermissions(permissions, new MultiplePermissionsRequestor.Callback() {
                @Override
                public void onPermissionResult(MultiplePermissionsGrantResult multiplePermissionsGrantResult) {
                    showToastMessage("grantResult=" + multiplePermissionsGrantResult);
                }
            });
        });

        ManageOverlayPermissionRequestor manageOverlayPermissionRequestor = new ManageOverlayPermissionRequestor();
        manageOverlayPermissionRequestor.observeLifecycle(this);
        binding.reqManageOverlayPermission.setOnClickListener(v -> manageOverlayPermissionRequestor.requestSystemPermission(
                null, grantResult -> showToastMessage("manage overlay permission grantResult=" + grantResult)));

        ActivityResultLauncher<Void> pickImageLauncher = registerForActivityResult(GetContent.anyImage(),
                result -> showSnackbarMessage("picked image uri=" + result));

        binding.buttonPickImageGetContent.setOnClickListener(v -> pickImageLauncher.launch(null));

        ActivityResultLauncher<Void> pickVideoLauncher = registerForActivityResult(GetContent.mp4Video(),
                result -> showSnackbarMessage("picked video uri=" + result));

        binding.buttonPickVideoGetContent.setOnClickListener(v -> pickVideoLauncher.launch(null));

        AppUpdateHandlerFactory appUpdateHandlerFactory = new AppUpdateHandlerFactory(this);
        binding.buttonCheckFlexibleUpdate.setOnClickListener(v -> {
            AppUpdateHandler appUpdateHandler = appUpdateHandlerFactory.fakePlayStoreFlexibleUpdateAvailableToDownloadAndInstall();
            checkFlexibleUpdate(appUpdateHandler);
        });
        binding.buttonCheckFlexibleUpdateDownloadFails.setOnClickListener(v -> {
            AppUpdateHandler appUpdateHandler = appUpdateHandlerFactory.fakePlayStoreFlexibleUpdateAvailableToDownloadButDownloadFails();
            checkFlexibleUpdate(appUpdateHandler);
        });
        binding.buttonCheckFlexibleUpdateInstallFails.setOnClickListener(v -> {
            AppUpdateHandler appUpdateHandler = appUpdateHandlerFactory.fakePlayStoreFlexibleUpdateAvailableToDownloadButInstall();
            checkFlexibleUpdate(appUpdateHandler);
        });
        binding.buttonCheckImmediateUpdate.setOnClickListener(v -> checkImmediateUpdate(appUpdateHandlerFactory));

        setupOpenSettings();
    }

    private void setupOpenSettings() {
        binding.buttonOpenSettings.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), SettingsActivity.class);
            v.getContext().startActivity(intent);
        });
    }

    private void checkImmediateUpdate(AppUpdateHandlerFactory appUpdateHandlerFactory) {
        AppUpdateHandler appUpdateHandler = appUpdateHandlerFactory.fakePlayStoreImmediateUpdateAvailableToInstall();
        appUpdateHandler.observeLifecycle(this);
        appUpdateHandler.checkAppUpdateAvailability(AppUpdateAvailabilityCriteria.isAppUpdateAllowedOfType(IMMEDIATE), new AppUpdateAvailabilityChecker.Callback() {

            @Override
            public void onAppUpdateAvailable(AppUpdateAvailability appUpdateAvailability) {

                PlayStoreAppUpdateRequest appUpdateRequest = new PlayStoreAppUpdateRequest(
                        IMMEDIATE,
                        MainActivity.this,
                        1,
                        (PlayStoreAppUpdateAvailability) appUpdateAvailability);

                appUpdateHandler.requestAppUpdate(appUpdateRequest);
            }

            @Override
            public void onAppUpdateAvailableCheckFailed(Throwable error) {
                showToastMessage(Throwables.getStackTraceAsString(error));
            }

            @Override
            public void onAppUpdateCriteriaNotMet() {
                showSnackbarMessage("onAppUpdateCriteriaNotMet");
            }
        });
    }

    private void checkFlexibleUpdate(AppUpdateHandler appUpdateHandler) {
        appUpdateHandler.observeLifecycle(this);
        appUpdateHandler.addObserver(new FlexibleAppUpdateObserver(appUpdateHandler));
        appUpdateHandler.checkAppUpdateAvailability(AppUpdateAvailabilityCriteria.isAppUpdateAllowedOfType(FLEXIBLE), new FlexibleAppAvailabilityCallback(appUpdateHandler));
    }

    private void print(Bitmap resource) {

        IntentPrintImageRequest printImageRequest = new IntentPrintImageRequest.Builder()
                .setContext(MainActivity.this)
                .setScaleMode(PrintHelper.SCALE_MODE_FIT)
                .setJobName(PRINT_JOB_NAME)
                .setBitmap(resource)
                .setOrientation(PrintHelper.ORIENTATION_PORTRAIT)
                .setColorMode(PrintHelper.COLOR_MODE_COLOR)
                .build();

        ImagePrinter.Listener listener = () -> showSnackbarMessage("print image result callback");
        new IntentImagePrinter().print(printImageRequest, listener);
    }

    private void showSnackbarMessage(String message) {
        makeSnackbar(message).show();
    }

    private Snackbar makeSnackbar(String message) {
        return makeSnackbar(message, Snackbar.LENGTH_SHORT);
    }

    private Snackbar makeSnackbar(String message, int length) {
        return Snackbar.make(binding.getRoot(), message, length);
    }

    private void showToastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private void pickImage(ActivityResultCallback<Uri> uriActivityResultCallback) {
        intentMediaPicker.pickMedia(PickMediaRequest.builder()
                .ofType(PickableMediaType.IMAGE)
                .pick(PickMediaCount.SINGLE)
                .withMediaResultCallback(uriActivityResultCallback)
                .build());
    }

    @Override
    public AppCompatActivity getAppCompatActivity() {
        return this;
    }

    private class FlexibleAppUpdateObserver implements AppUpdateRequestor.Observer {

        private final AppUpdateHandler appUpdateHandler;

        private FlexibleAppUpdateObserver(AppUpdateHandler appUpdateHandler) {
            this.appUpdateHandler = appUpdateHandler;
        }

        @Override
        public void onAppUpdateDownloadFailure() {
            showSnackbarMessage("onAppUpdateDownloadFailure");
        }

        @Override
        public void onAppUpdateUnknownFailure() {
            showSnackbarMessage("onAppUpdateUnknownFailure");
        }

        @Override
        public void onAppUpdateDownloadProgressChange(long bytesDownloaded, long totalBytesToDownload) {
            showSnackbarMessage("Downloading app update... " + bytesDownloaded + "/" + totalBytesToDownload);
        }

        @Override
        public void onAppUpdateDownloaded() {
            makeSnackbar("An update has been downloaded.", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Install", v -> appUpdateHandler.installDownloadedUpdate())
                    .show();
        }

        @Override
        public void onAppUpdateInstallFailure() {
            showSnackbarMessage("onAppUpdateInstallFailed");
        }

        @Override
        public void onRequestAppUpdateFailure(Throwable error) {
            showSnackbarMessage("onAppUpdateRequestFailed\n" + Throwables.getStackTraceAsString(error));
        }

        @Override
        public void onInstallingAppUpdate() {
            showSnackbarMessage("Installing app update");
        }

        @Override
        public void onAppUpdateInstalled() {
            showSnackbarMessage("App has been successfully updated!");
        }

        @Override
        public void onImmediateAppUpdateInProgress(AppUpdateAvailability appUpdateAvailability) {
            showSnackbarMessage("immediate app update in progress appUpdateAvailability=" + appUpdateAvailability);
            appUpdateHandler.requestAppUpdate(new PlayStoreAppUpdateRequest(
                    IMMEDIATE,
                    MainActivity.this,
                    1,
                    (PlayStoreAppUpdateAvailability) appUpdateAvailability));
        }

        @Override
        public void onAppUpdatePendingDownload() {
            showSnackbarMessage("onAppUpdatePendingDownload");
        }
    }

    private class FlexibleAppAvailabilityCallback implements AppUpdateAvailabilityChecker.Callback {

        private final AppUpdateHandler appUpdateHandler;

        private FlexibleAppAvailabilityCallback(AppUpdateHandler appUpdateHandler) {
            this.appUpdateHandler = appUpdateHandler;
        }

        @Override
        public void onAppUpdateAvailable(AppUpdateAvailability appUpdateAvailability) {
            makeSnackbar("An update is available for the app", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Download", v -> {

                        PlayStoreAppUpdateRequest appUpdateRequest = new PlayStoreAppUpdateRequest(
                                FLEXIBLE,
                                MainActivity.this,
                                1,
                                (PlayStoreAppUpdateAvailability) appUpdateAvailability);

                        appUpdateHandler.requestAppUpdate(appUpdateRequest);
                    })
                    .show();
        }

        @Override
        public void onAppUpdateAvailableCheckFailed(Throwable error) {
            showToastMessage(Throwables.getStackTraceAsString(error));
        }

        @Override
        public void onAppUpdateCriteriaNotMet() {
            showSnackbarMessage("onAppUpdateCriteriaNotMet");
        }
    }


}