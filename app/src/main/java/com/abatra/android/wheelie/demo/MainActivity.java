package com.abatra.android.wheelie.demo;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.abatra.android.wheelie.activity.ActivityResultRegistrar;
import com.abatra.android.wheelie.media.picker.IntentMediaPicker;
import com.abatra.android.wheelie.media.picker.PickMediaCount;
import com.abatra.android.wheelie.media.picker.PickMediaRequest;
import com.abatra.android.wheelie.media.picker.PickableMediaType;

import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements ActivityResultRegistrar {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        IntentMediaPicker intentMediaPicker = new IntentMediaPicker();
        intentMediaPicker.setActivityResultRegistrar(this);
        intentMediaPicker.pickMedia(PickMediaRequest.builder()
                .pick(PickMediaCount.MULTIPLE)
                .ofType(PickableMediaType.IMAGE)
                .withActivityResultRegistrar(this)
                .withMultipleMediaResultCallback(result -> Timber.d("result=%s", result))
                .build());
    }

    @Override
    public Context getContext() {
        return this;
    }
}