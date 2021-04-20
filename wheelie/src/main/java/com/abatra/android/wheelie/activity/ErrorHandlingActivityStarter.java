package com.abatra.android.wheelie.activity;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;

import androidx.activity.result.ActivityResultLauncher;
import androidx.fragment.app.Fragment;

import timber.log.Timber;

public class ErrorHandlingActivityStarter implements ActivityStarter {

    @Override
    public void startActivity(Context context, Intent intent, StartActivityErrorHandler errorHandler) {
        startActivityWithErrorHandling(() -> context.startActivity(intent), errorHandler);
    }

    private void startActivityWithErrorHandling(Runnable runnable, StartActivityErrorHandler errorHandler) {
        try {
            runnable.run();
        } catch (ActivityNotFoundException activityNotFoundException) {
            Timber.e(activityNotFoundException);
            errorHandler.onActivityNotFoundError(activityNotFoundException);
        } catch (Throwable error) {
            Timber.e(error);
            errorHandler.onError(error);
        }
    }

    @Override
    public void startActivity(Fragment fragment, Intent intent, StartActivityErrorHandler errorHandler) {
        startActivityWithErrorHandling(() -> fragment.startActivity(intent), errorHandler);
    }

    @Override
    public void startActivity(Activity activity, Intent intent, StartActivityErrorHandler errorHandler) {
        startActivityWithErrorHandling(() -> activity.startActivity(intent), errorHandler);
    }

    @Override
    public <I> void launch(ActivityResultLauncher<I> activityResultLauncher, I input, StartActivityErrorHandler errorHandler) {
        startActivityWithErrorHandling(() -> activityResultLauncher.launch(input), errorHandler);
    }
}
