package com.abatra.android.wheelie.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public interface ActivityStarter {

    static ActivityStarter of(Fragment fragment) {
        return new SafeActivityStarter(fragment::startActivity);
    }

    static ActivityStarter of(Context context) {
        return new SafeActivityStarter(context::startActivity);
    }

    static ActivityStarter of(Activity activity) {
        return new SafeActivityStarter(activity::startActivity);
    }

    void setActivityNotFoundErrorHandler(@NonNull ActivityNotFoundErrorHandler activityNotFoundErrorHandler);

    void startActivity(Intent intent);

    <I> void launch(ActivityResultLauncher<I> activityResultLauncher, I input);
}
