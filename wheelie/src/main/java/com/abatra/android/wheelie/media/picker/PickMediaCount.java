package com.abatra.android.wheelie.media.picker;

import androidx.activity.result.ActivityResultLauncher;

public enum PickMediaCount {
    MULTIPLE {
        @Override
        public ActivityResultLauncher<String> getActivityResultLauncher(IntentMediaPicker intentMediaPicker) {
            return intentMediaPicker.getMultipleContentLauncher;
        }
    };

    public abstract ActivityResultLauncher<String> getActivityResultLauncher(IntentMediaPicker intentMediaPicker);
}
