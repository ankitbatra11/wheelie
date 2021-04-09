package com.abatra.android.wheelie.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;

import java.util.function.Consumer;

import timber.log.Timber;

class SafeActivityStarter implements ActivityStarter {

    private final Consumer<Intent> intentStarter;
    private ActivityNotFoundErrorHandler activityNotFoundErrorHandler = ActivityNotFoundErrorHandler.NO_OP;

    protected SafeActivityStarter(Consumer<Intent> intentStarter) {
        this.intentStarter = intentStarter;
    }

    @Override
    public void setActivityNotFoundErrorHandler(@NonNull ActivityNotFoundErrorHandler activityNotFoundErrorHandler) {
        this.activityNotFoundErrorHandler = activityNotFoundErrorHandler;
    }

    @Override
    public void startActivity(Intent intent) {
        startActivitySilently(() -> intentStarter.accept(intent), intent);
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
