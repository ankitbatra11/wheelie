package com.abatra.android.wheelie.demo;

import android.content.Context;

import androidx.annotation.NonNull;

import com.abatra.android.wheelie.firebase.CrashlyticsTimberTree;
import com.abatra.android.wheelie.logger.TimberInitializer;
import com.google.firebase.crashlytics.FirebaseCrashlytics;

import timber.log.Timber;

public class LoggerInitializer extends TimberInitializer {

    @NonNull
    @Override
    public Void create(@NonNull Context context) {
        Void result = super.create(context);
        Timber.i("Timber is initialized!");
        return result;
    }

    @Override
    protected Timber.Tree getReleaseTree() {
        return new CrashlyticsTimberTree(FirebaseCrashlytics.getInstance());
    }

    @Override
    protected boolean isDebug() {
        return BuildConfig.DEBUG;
    }
}
