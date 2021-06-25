package com.abatra.android.wheelie.picker;

import androidx.activity.result.ActivityResultLauncher;

public enum PickMediaCount {
    MULTIPLE {
        @Override
        public ActivityResultLauncher<String> getActivityResultLauncher(IntentMediaPicker intentMediaPicker) {
            return intentMediaPicker.getGetMultipleContentLauncher();
        }
    },
    SINGLE {
        @Override
        public ActivityResultLauncher<String> getActivityResultLauncher(IntentMediaPicker intentMediaPicker) {
            return intentMediaPicker.getGetContentLauncher();
        }
    };

    public abstract ActivityResultLauncher<String> getActivityResultLauncher(IntentMediaPicker intentMediaPicker);
}
