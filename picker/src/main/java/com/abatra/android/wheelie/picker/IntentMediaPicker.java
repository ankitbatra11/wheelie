package com.abatra.android.wheelie.picker;

import android.net.Uri;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;

import com.abatra.android.wheelie.lifecycle.observer.ILifecycleObserver;
import com.abatra.android.wheelie.lifecycle.owner.ILifecycleOwner;

import java.util.List;

public class IntentMediaPicker implements MediaPicker, ILifecycleObserver {

    private static final IntentMediaPicker INSTANCE = new IntentMediaPicker();

    @Nullable
    private ActivityResultLauncher<String> getMultipleContentLauncher;
    @Nullable
    private ActivityResultCallback<List<Uri>> multipleContentResultCallback;

    @Nullable
    private ActivityResultLauncher<String> getContentLauncher;
    @Nullable
    private ActivityResultCallback<Uri> getContentResultCallback;

    private IntentMediaPicker() {
    }

    public static IntentMediaPicker getInstance() {
        return INSTANCE;
    }

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
