package com.abatra.android.wheelie.activity;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import timber.log.Timber;

public class ErrorHandlingActivityStarter implements ActivityStarter {

    private ActivityNotFoundErrorHandler activityNotFoundErrorHandler = ActivityNotFoundErrorHandler.NO_OP;

    @Override
    public void setActivityNotFoundErrorHandler(@NonNull ActivityNotFoundErrorHandler activityNotFoundErrorHandler) {
        this.activityNotFoundErrorHandler = activityNotFoundErrorHandler;
    }

    @Override
    public void startActivity(Context context, Intent intent) {
        startActivitySilently(() -> context.startActivity(intent), intent);
    }

    @Override
    public void startActivity(Fragment fragment, Intent intent) {
        startActivitySilently(() -> fragment.startActivity(intent), intent);
    }

    @Override
    public void startActivity(Activity activity, Intent intent) {
        startActivitySilently(() -> activity.startActivity(intent), intent);
    }

    public void startActivitySilently(Runnable startActivity, Object infoForActivityNotFoundError) {
        try {
            startActivity.run();
        } catch (ActivityNotFoundException e) {
            Timber.e(e);
            activityNotFoundErrorHandler.handleActivityNotFoundError(infoForActivityNotFoundError);
        } catch (Throwable t) {
            Timber.e(t);
        }
    }

    @Override
    public <I> void launch(ActivityResultLauncher<I> activityResultLauncher, I input) {
        startActivitySilently(() -> activityResultLauncher.launch(input), input);
    }
}
