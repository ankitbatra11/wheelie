package com.abatra.android.wheelie.activity;

import android.content.Intent;

import androidx.activity.result.ActivityResultLauncher;

public interface ActivityNotFoundErrorHandler {

    ActivityNotFoundErrorHandler NO_OP = new ActivityNotFoundErrorHandler() {
    };

    /**
     * @param info Could be intent or input.
     * @see ActivityStarter#startActivity(Intent)
     * @see ActivityStarter#launch(ActivityResultLauncher, Object)
     */
    default void handleActivityNotFoundError(Object info) {
    }
}
