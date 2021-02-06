package com.abatra.android.wheelie.media.picker;

import android.net.Uri;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;

import com.abatra.android.wheelie.lifecycle.ILifecycleObserver;
import com.abatra.android.wheelie.lifecycle.ILifecycleOwner;

import java.util.List;

public class IntentMediaPicker implements MediaPicker, ILifecycleObserver {

    @Nullable
    private ActivityResultLauncher<String> getMultipleContentLauncher;
    @Nullable
    private ActivityResultCallback<List<Uri>> multipleContentResultCallback;

    @Nullable
    private ActivityResultLauncher<String> getContentLauncher;
    @Nullable
    private ActivityResultCallback<Uri> getContentResultCallback;

    @Nullable
    ActivityResultLauncher<String> getGetMultipleContentLauncher() {
        return getMultipleContentLauncher;
    }

    @Nullable
    ActivityResultLauncher<String> getGetContentLauncher() {
        return getContentLauncher;
    }

    @Override
    public void observeLifecycle(ILifecycleOwner lifecycleOwner) {
        lifecycleOwner.getLifecycle().addObserver(this);
        ActivityResultContracts.GetMultipleContents contract = new ActivityResultContracts.GetMultipleContents();
        getMultipleContentLauncher = lifecycleOwner.registerForActivityResult(contract, result -> {
            if (multipleContentResultCallback != null) {
                multipleContentResultCallback.onActivityResult(result);
            }
        });
        getContentLauncher = lifecycleOwner.registerForActivityResult(new ActivityResultContracts.GetContent(), result -> {
            if (getContentResultCallback != null) {
                getContentResultCallback.onActivityResult(result);
            }
        });
    }

    @Override
    public void pickMedia(PickMediaRequest pickMediaRequest) {
        multipleContentResultCallback = pickMediaRequest.getMultipleMediaResultCallback();
        getContentResultCallback = pickMediaRequest.getMediaResultCallback();
        String mimeType = pickMediaRequest.getPickableMediaType().getMimeType();
        pickMediaRequest.getPickMediaCount().getActivityResultLauncher(this).launch(mimeType);
    }

    @Override
    public void onDestroy() {
        getMultipleContentLauncher = null;
        multipleContentResultCallback = null;
        getContentLauncher = null;
        getContentResultCallback = null;
    }
}
