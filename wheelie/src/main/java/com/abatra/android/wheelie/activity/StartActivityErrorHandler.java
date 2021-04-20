package com.abatra.android.wheelie.activity;

import android.content.ActivityNotFoundException;

public interface StartActivityErrorHandler {

    StartActivityErrorHandler DO_NOTHING = new StartActivityErrorHandler() {
    };

    default void onActivityNotFoundError(ActivityNotFoundException activityNotFoundException) {
    }

    default void onError(Throwable error) {
    }
}
