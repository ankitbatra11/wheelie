package com.abatra.android.wheelie.media.picker;

import android.net.Uri;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;

import com.abatra.android.wheelie.activity.ActivityResultRegistrar;

import java.util.List;

public class IntentMediaPicker implements MediaPicker {

    private static final IntentMediaPicker INSTANCE = new IntentMediaPicker();

    private IntentMediaPicker() {
    }

    public static IntentMediaPicker getInstance() {
        return INSTANCE;
    }

    @Override
    public void pickMedia(PickMediaRequest pickMediaRequest) {
        ActivityResultRegistrar registrar = pickMediaRequest.getActivityResultRegistrar();
        ActivityResultContract<String, List<Uri>> contract = pickMediaRequest.getPickMediaCount().getActivityResultContract();
        ActivityResultCallback<List<Uri>> callback = pickMediaRequest.getMultipleMediaResultCallback();
        ActivityResultLauncher<String> activityResultLauncher = registrar.registerForActivityResult(contract, callback);
        activityResultLauncher.launch(pickMediaRequest.getPickableMediaType().getMimeType());
    }

}
