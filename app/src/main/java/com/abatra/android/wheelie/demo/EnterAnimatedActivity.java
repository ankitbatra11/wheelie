package com.abatra.android.wheelie.demo;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Window;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.abatra.android.wheelie.animation.SharedAxisMotion;
import com.abatra.android.wheelie.demo.databinding.ActivityEnterAnimatedBinding;

public class EnterAnimatedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            requestWindowFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
            SharedAxisMotion.X.setEnterAnimation(this);
        }

        super.onCreate(savedInstanceState);

        ActivityEnterAnimatedBinding binding = ActivityEnterAnimatedBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
    }
}
