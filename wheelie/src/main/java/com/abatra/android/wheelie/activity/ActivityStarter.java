package com.abatra.android.wheelie.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public interface ActivityStarter {

    void setActivityNotFoundErrorHandler(@NonNull ActivityNotFoundErrorHandler activityNotFoundErrorHandler);

    void startActivity(Context context, Intent intent);

    void startActivity(Fragment fragment, Intent intent);

    void startActivity(Activity activity, Intent intent);

    <I> void launch(ActivityResultLauncher<I> activityResultLauncher, I input);
}
