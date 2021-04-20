package com.abatra.android.wheelie.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.activity.result.ActivityResultLauncher;
import androidx.fragment.app.Fragment;

public interface ActivityNotFoundErrorHandler {

    ActivityNotFoundErrorHandler NO_OP = new ActivityNotFoundErrorHandler() {
    };

    /**
     * @param info Could be intent or input.
     * @see ActivityStarter#startActivity(Context, Intent)
     * @see ActivityStarter#startActivity(Activity, Intent)
     * @see ActivityStarter#startActivity(Fragment, Intent)
     * @see ActivityStarter#launch(ActivityResultLauncher, Object)
     */
    default void handleActivityNotFoundError(Object info) {
    }
}
