package com.abatra.android.wheelie.media.picker;

import android.net.Uri;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.abatra.android.wheelie.activity.ActivityResultRegistrar;
import com.abatra.android.wheelie.lifecycle.ILifecycleObserver;

import java.util.List;

public class IntentMediaPicker implements MediaPicker, ILifecycleObserver {

    ActivityResultLauncher<String> getMultipleContentLauncher;
    private ActivityResultCallback<List<Uri>> multipleContentResultCallback;

    public void setActivityResultRegistrar(ActivityResultRegistrar activityResultRegistrar) {
        activityResultRegistrar.getLifecycle().addObserver(this);
        ActivityResultContracts.GetMultipleContents contract = new ActivityResultContracts.GetMultipleContents();
        getMultipleContentLauncher = activityResultRegistrar.registerForActivityResult(contract, result -> {
            if (multipleContentResultCallback != null) {
                multipleContentResultCallback.onActivityResult(result);
            }
        });
    }

    @Override
    public void pickMedia(PickMediaRequest pickMediaRequest) {
        multipleContentResultCallback = pickMediaRequest.getMultipleMediaResultCallback();
        String mimeType = pickMediaRequest.getPickableMediaType().getMimeType();
        pickMediaRequest.getPickMediaCount().getActivityResultLauncher(this).launch(mimeType);
    }

    @Override
    public void onDestroy() {
        getMultipleContentLauncher = null;
        multipleContentResultCallback = null;
    }
}
