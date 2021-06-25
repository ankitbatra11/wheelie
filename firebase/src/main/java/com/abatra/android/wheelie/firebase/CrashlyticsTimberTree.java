package com.abatra.android.wheelie.firebase;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.crashlytics.FirebaseCrashlytics;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;
import java.util.Optional;

import timber.log.Timber;

public class CrashlyticsTimberTree extends Timber.Tree {

    private final FirebaseCrashlytics firebaseCrashlytics;

    public CrashlyticsTimberTree(FirebaseCrashlytics firebaseCrashlytics) {
        this.firebaseCrashlytics = firebaseCrashlytics;
    }

    @Override
    protected void log(int priority, @Nullable String tag, @NotNull String message, @Nullable Throwable error) {
        if (priority >= Log.WARN) {
            logPriorityMessage(priority, tag, message, error);
        }
    }

    private void logPriorityMessage(int priority, @Nullable String tag, @NonNull String message, @Nullable Throwable error) {
        firebaseCrashlytics.log(getFormattedMessage(priority, tag, message));
        Optional.ofNullable(error).ifPresent(firebaseCrashlytics::recordException);
    }

    private String getFormattedMessage(int priority, @Nullable String tag, @NonNull String message) {
        return String.format(Locale.ENGLISH,
                "%d/%s: %s", // format https://firebase.google.com/docs/crashlytics/upgrade-sdk?platform=android#firebase-crashlytics-sdk_2
                priority, tag, message);
    }
}
