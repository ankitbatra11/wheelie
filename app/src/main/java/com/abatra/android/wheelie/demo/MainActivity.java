package com.abatra.android.wheelie.demo;

import static com.abatra.android.wheelie.appUpdate.AppUpdateType.FLEXIBLE;
import static com.abatra.android.wheelie.appUpdate.AppUpdateType.IMMEDIATE;
import static com.abatra.android.wheelie.core.activity.resultContracts.OpenMediaActivityResultContract.OpenableMedia;
import static com.abatra.android.wheelie.core.activity.resultContracts.OpenSettingsScreenActivityResultContract.wirelessSettings;
import static com.abatra.android.wheelie.core.activity.resultContracts.ShareMediaActivityResultContract.ShareableMedia;
import static com.abatra.android.wheelie.core.content.Intents.openAppDetailsSettings;

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
import androidx.print.PrintHelper;

import com.abatra.android.wheelie.appUpdate.AppUpdateAvailability;
import com.abatra.android.wheelie.appUpdate.AppUpdateAvailabilityChecker;
import com.abatra.android.wheelie.appUpdate.AppUpdateAvailabilityCriteria;
import com.abatra.android.wheelie.appUpdate.AppUpdateHandler;
import com.abatra.android.wheelie.appUpdate.AppUpdateHandlerFactory;
import com.abatra.android.wheelie.appUpdate.AppUpdateRequestor;
import com.abatra.android.wheelie.appUpdate.playStore.PlayStoreAppUpdateAvailability;
import com.abatra.android.wheelie.appUpdate.playStore.PlayStoreAppUpdateRequest;
import com.abatra.android.wheelie.core.MimeTypes;
import com.abatra.android.wheelie.core.activity.ErrorHandlingActivityStarter;
import com.abatra.android.wheelie.core.activity.resultContracts.AttachDataActivityResultContract;
import com.abatra.android.wheelie.core.activity.resultContracts.AttachDataActivityResultContract.AttachableData;
import com.abatra.android.wheelie.core.activity.resultContracts.GetContentActivityResultContract;
import com.abatra.android.wheelie.core.activity.resultContracts.IntentActivityResultContract;
import com.abatra.android.wheelie.core.activity.resultContracts.OpenMediaActivityResultContract;
import com.abatra.android.wheelie.core.activity.resultContracts.ShareMediaActivityResultContract;
import com.abatra.android.wheelie.core.anim.SharedAxisMotion;
import com.abatra.android.wheelie.core.network.InternetConnectivityChecker;
import com.abatra.android.wheelie.demo.databinding.ActivityMainBinding;
import com.abatra.android.wheelie.lifecycle.network.LifecycleInternetConnectivityChecker;
import com.abatra.android.wheelie.lifecycle.owner.ILifecycleOwner;
import com.abatra.android.wheelie.mayI.HybridPermissionRequestor;
import com.abatra.android.wheelie.mayI.ManageOverlayPermissionRequestor;
import com.abatra.android.wheelie.mayI.ManifestMultiplePermissionsRequestor;
import com.abatra.android.wheelie.mayI.ManifestSinglePermissionRequestor;
import com.abatra.android.wheelie.mayI.MultiplePermissionsGrantResult;
import com.abatra.android.wheelie.mayI.MultiplePermissionsRequestor;
import com.abatra.android.wheelie.mayI.OpenAppDetailsPermissionRequestor;
import com.abatra.android.wheelie.picker.IntentMediaPicker;
import com.abatra.android.wheelie.picker.PickMediaCount;
import com.abatra.android.wheelie.picker.PickMediaRequest;
import com.abatra.android.wheelie.picker.PickableMediaType;
import com.abatra.android.wheelie.printer.ImagePrinter;
import com.abatra.android.wheelie.printer.IntentImagePrinter;
import com.abatra.android.wheelie.printer.IntentPrintImageRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.snackbar.Snackbar;
import com.google.common.base.Throwables;

import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    private static final String PRINT_JOB_NAME = "print picked image";

    private final IntentMediaPicker intentMediaPicker = IntentMediaPicker.getInstance();
    private InternetConnectivityChecker internetConnectivityChecker;
    private ActivityResultLauncher<AttachableData> attachDataLauncher;
    private ActivityResultLauncher<OpenableMedia> openMediaLauncher;
    private ActivityResultLauncher<ShareableMedia> shareMediaLauncher;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LifecycleInternetConnectivityChecker internetConnectivityChecker = LifecycleInternetConnectivityChecker.newInstance(this);
        this.internetConnectivityChecker = internetConnectivityChecker;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            requestWindowFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
            SharedAxisMotion.X.setExitAnimation(this);
        }

        super.onCreate(savedInstanceState);

        OpenAppDetailsPermissionRequestor openAppDetailsPermissionRequestor = new OpenAppDetailsPermissionRequestor(
                new HybridPermissionRequestor(
                        new ManifestSinglePermissionRequestor(),
                        new ManifestMultiplePermissionsRequestor()
                ),
                new ErrorHandlingActivityStarter()
        );

        openAppDetailsPermissionRequestor.observeLifecycle(ILifecycleOwner.activity(this));

        attachDataLauncher = registerForActivityResult(new AttachDataActivityResultContract(),
                result -> showSnackbarMessage("Attach data result callback"));

        openMediaLauncher = registerForActivityResult(new OpenMediaActivityResultContract(),
                result -> showSnackbarMessage("open media result callback"));

        shareMediaLauncher = registerForActivityResult(new ShareMediaActivityResultContract(),
                result -> showSnackbarMessage("share media result callback"));

        binding = ActivityMainBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        binding.recyclerViewDemoButton.setOnClickListener(v -> startActivity(new Intent(v.getContext(), RecyclerViewAdapterDemoActivity.class)));

        intentMediaPicker.observeLifecycle(ILifecycleOwner.activity(this));
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

        binding.checkInternetConnectionBtn.setOnClickListener(v -> showConnectivityStatus(this.internetConnectivityChecker.isConnectedToInternet()));
        this.internetConnectivityChecker.addObserver(this::showConnectivityStatus);
        internetConnectivityChecker.observeLifecycle(ILifecycleOwner.activity(this));

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
            AttachableData input = new AttachableData(result, MimeTypes.IMAGE_ANY);
            attachDataLauncher.launch(input);
        }));
        binding.openImage.setOnClickListener(v -> pickImage(result -> {
            OpenableMedia input = new OpenableMedia(result, MimeTypes.IMAGE_ANY);
            openMediaLauncher.launch(input);
        }));
        binding.shareImage.setOnClickListener(v -> pickImage(result -> {
            ShareableMedia input = new ShareableMedia(MimeTypes.IMAGE_ANY, result);
            shareMediaLauncher.launch(input);
        }));

        binding.reqCameraPermission.setOnClickListener(v -> {
            String permission = Manifest.permission.CAMERA;
            openAppDetailsPermissionRequestor.requestSystemPermission(permission, grantResult -> showToastMessage("grantResult=" + grantResult));
        });

        ActivityResultLauncher<Intent> appDetailsLauncher = registerForActivityResult(
                IntentActivityResultContract.INSTANCE,
                result -> showToastMessage("app details result=" + result));

        binding.launchAppDetailsSettings.setOnClickListener(v -> appDetailsLauncher.launch(openAppDetailsSettings(this)));

        binding.reqMultiplePermissions.setOnClickListener(v -> {
            String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO};
            openAppDetailsPermissionRequestor.requestSystemPermissions(permissions, new MultiplePermissionsRequestor.Callback() {
                @Override
                public void onPermissionResult(MultiplePermissionsGrantResult multiplePermissionsGrantResult) {
                    showToastMessage("grantResult=" + multiplePermissionsGrantResult);
                }
            });
        });

        ManageOverlayPermissionRequestor manageOverlayPermissionRequestor = new ManageOverlayPermissionRequestor();
        manageOverlayPermissionRequestor.observeLifecycle(ILifecycleOwner.activity(this));
        binding.reqManageOverlayPermission.setOnClickListener(v -> manageOverlayPermissionRequestor.requestSystemPermission(
                null, grantResult -> showToastMessage("manage overlay permission grantResult=" + grantResult)));

        ActivityResultLauncher<Void> pickImageLauncher = registerForActivityResult(GetContentActivityResultContract.anyImage(),
                result -> showSnackbarMessage("picked image uri=" + result));

        binding.buttonPickImageGetContent.setOnClickListener(v -> pickImageLauncher.launch(null));

        ActivityResultLauncher<Void> pickVideoLauncher = registerForActivityResult(GetContentActivityResultContract.mp4Video(),
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

    private void showConnectivityStatus(boolean connected) {
        Snackbar snackbar = Snackbar.make(binding.getRoot(), String.valueOf(connected), Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

    @Override
    protected void onDestroy() {
        internetConnectivityChecker.stopChecking();
        super.onDestroy();
    }

    private void setupOpenSettings() {
        binding.buttonOpenSettings.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), SettingsActivity.class);
            v.getContext().startActivity(intent);
        });
    }

    private void checkImmediateUpdate(AppUpdateHandlerFactory appUpdateHandlerFactory) {
        AppUpdateHandler appUpdateHandler = appUpdateHandlerFactory.fakePlayStoreImmediateUpdateAvailableToInstall();
        appUpdateHandler.observeLifecycle(ILifecycleOwner.activity(this));
        appUpdateHandler.checkAppUpdateAvailability(AppUpdateAvailabilityCriteria.isAppUpdateAllowedOfType(IMMEDIATE), new AppUpdateAvailabilityChecker.Callback() {

            @Override
            public void onAppUpdateAvailable(AppUpdateAvailability updateAvailability) {

                PlayStoreAppUpdateRequest appUpdateRequest = new PlayStoreAppUpdateRequest(
                        IMMEDIATE,
                        MainActivity.this,
                        1,
                        (PlayStoreAppUpdateAvailability) updateAvailability);

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
        appUpdateHandler.observeLifecycle(ILifecycleOwner.activity(this));
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

        IntentImagePrinter.getInstance().print(printImageRequest, listener);
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
        public void onAppUpdateAvailable(AppUpdateAvailability updateAvailability) {
            makeSnackbar("An update is available for the app", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Download", v -> {

                        PlayStoreAppUpdateRequest appUpdateRequest = new PlayStoreAppUpdateRequest(
                                FLEXIBLE,
                                MainActivity.this,
                                1,
                                (PlayStoreAppUpdateAvailability) updateAvailability);

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