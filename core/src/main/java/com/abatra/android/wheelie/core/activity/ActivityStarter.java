package com.abatra.android.wheelie.core.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.activity.result.ActivityResultLauncher;
import androidx.fragment.app.Fragment;

public interface ActivityStarter {

    default void startActivity(Context context, Intent intent) {
        startActivity(context, intent, StartActivityErrorHandler.DO_NOTHING);
    }

    void startActivity(Context context, Intent intent, StartActivityErrorHandler errorHandler);

    default void startActivity(Fragment fragment, Intent intent) {
        startActivity(fragment, intent, StartActivityErrorHandler.DO_NOTHING);
    }

    void startActivity(Fragment fragment, Intent intent, StartActivityErrorHandler errorHandler);

    default void startActivity(Activity activity, Intent intent) {
        startActivity(activity, intent, StartActivityErrorHandler.DO_NOTHING);
    }

    void startActivity(Activity activity, Intent intent, StartActivityErrorHandler errorHandler);

    default <I> void launch(ActivityResultLauncher<I> activityResultLauncher, I input) {
        launch(activityResultLauncher, input, StartActivityErrorHandler.DO_NOTHING);
    }

    <I> void launch(ActivityResultLauncher<I> activityResultLauncher, I input, StartActivityErrorHandler errorHandler);
}
