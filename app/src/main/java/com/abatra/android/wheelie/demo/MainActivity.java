package com.abatra.android.wheelie.demo;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;

import com.abatra.android.wheelie.activity.ActivityResultRegistrar;
import com.abatra.android.wheelie.animation.AnimationAttributes;
import com.abatra.android.wheelie.animation.MaterialMotion;
import com.abatra.android.wheelie.demo.databinding.ActivityMainBinding;
import com.abatra.android.wheelie.media.picker.IntentMediaPicker;
import com.abatra.android.wheelie.media.picker.PickMediaCount;
import com.abatra.android.wheelie.media.picker.PickMediaRequest;
import com.abatra.android.wheelie.media.picker.PickableMediaType;

import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements ActivityResultRegistrar {

    private final IntentMediaPicker intentMediaPicker = new IntentMediaPicker();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            requestWindowFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
            MaterialMotion.sharedXAxis().applyExitAnimation(this, AnimationAttributes.defaultAttributes());
        }

        super.onCreate(savedInstanceState);

        ActivityMainBinding binding = ActivityMainBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        intentMediaPicker.setActivityResultRegistrar(this);
        binding.pickImage.setOnClickListener(v -> {

            PickMediaRequest pickMediaRequest = PickMediaRequest.builder()
                    .pick(PickMediaCount.MULTIPLE)
                    .ofType(PickableMediaType.IMAGE)
                    .withActivityResultRegistrar(MainActivity.this)
                    .withMultipleMediaResultCallback(result -> Timber.d("result=%s", result))
                    .build();

            intentMediaPicker.pickMedia(pickMediaRequest);
        });
        binding.launchEnterAnimatedScreen.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Intent intent = new Intent(v.getContext(), EnterAnimatedActivity.class);
                MaterialMotion.sharedXAxis().startActivityWithAnimation(intent, MainActivity.this);
            }
        });
    }

    @Override
    public Context getContext() {
        return this;
    }
}