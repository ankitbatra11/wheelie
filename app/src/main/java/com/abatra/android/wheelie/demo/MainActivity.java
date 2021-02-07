package com.abatra.android.wheelie.demo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Window;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.print.PrintHelper;

import com.abatra.android.wheelie.activity.ResultContracts;
import com.abatra.android.wheelie.animation.SharedAxisMotion;
import com.abatra.android.wheelie.demo.databinding.ActivityMainBinding;
import com.abatra.android.wheelie.lifecycle.ILifecycleOwner;
import com.abatra.android.wheelie.media.picker.IntentMediaPicker;
import com.abatra.android.wheelie.media.picker.PickMediaCount;
import com.abatra.android.wheelie.media.picker.PickMediaRequest;
import com.abatra.android.wheelie.media.picker.PickableMediaType;
import com.abatra.android.wheelie.media.printer.ImagePrinter;
import com.abatra.android.wheelie.media.printer.IntentImagePrinter;
import com.abatra.android.wheelie.media.printer.IntentPrintImageRequest;
import com.abatra.android.wheelie.network.InternetConnectionObserver;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.snackbar.Snackbar;

import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements ILifecycleOwner {

    public static final String PRINT_JOB_NAME = "print picked image";
    private final IntentMediaPicker intentMediaPicker = new IntentMediaPicker();
    private final InternetConnectionObserver connectionObserver = InternetConnectionObserver.newInstance(this);
    private ActivityResultLauncher<ResultContracts.MediaInfo> attachDataLauncher;
    private ActivityResultLauncher<ResultContracts.MediaInfo> openMediaLauncher;
    private ActivityResultLauncher<ResultContracts.MediaInfo> shareMediaLauncher;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            requestWindowFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
            SharedAxisMotion.X.setExitAnimation(this);
        }

        super.onCreate(savedInstanceState);

        attachDataLauncher = registerForActivityResult(new ResultContracts.AttachData(),
                result -> showMessage("Attach data result callback"));

        openMediaLauncher = registerForActivityResult(new ResultContracts.OpenMedia(),
                result -> showMessage("open media result callback"));

        shareMediaLauncher = registerForActivityResult(new ResultContracts.ShareMedia(),
                result -> showMessage("share media result callback"));

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

        connectionObserver.observeLifecycle(this);
        binding.checkInternetConnectionBtn.setOnClickListener(v -> {
            String message = String.valueOf(connectionObserver.isConnectedToInternet());
            Snackbar.make(v, message, Snackbar.LENGTH_SHORT).show();
        });

        ActivityResultLauncher<Void> launcher = registerForActivityResult(ResultContracts.OpenSettingsScreen.wirelessSettings(), result -> {
            ConstraintLayout view = binding.getRoot();
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
            ResultContracts.MediaInfo image = ResultContracts.MediaInfo.image(result);
            attachDataLauncher.launch(image);
        }));
        binding.openImage.setOnClickListener(v -> pickImage(result -> {
            ResultContracts.MediaInfo image = ResultContracts.MediaInfo.image(result);
            openMediaLauncher.launch(image);
        }));
        binding.shareImage.setOnClickListener(v -> pickImage(result -> {
            ResultContracts.MediaInfo image = ResultContracts.MediaInfo.image(result);
            shareMediaLauncher.launch(image);
        }));
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

        ImagePrinter.Listener listener = () -> showMessage("print image result callback");
        new IntentImagePrinter().print(printImageRequest, listener);
    }

    private void showMessage(String message) {
        Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_SHORT).show();
    }

    private void pickImage(ActivityResultCallback<Uri> uriActivityResultCallback) {
        intentMediaPicker.pickMedia(PickMediaRequest.builder()
                .ofType(PickableMediaType.IMAGE)
                .pick(PickMediaCount.SINGLE)
                .withMediaResultCallback(uriActivityResultCallback)
                .build());
    }

    @Override
    public Activity getActivity() {
        return this;
    }
}