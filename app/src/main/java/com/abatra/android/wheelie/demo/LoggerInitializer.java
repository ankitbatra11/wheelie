package com.abatra.android.wheelie.demo;

import android.content.Context;

import androidx.annotation.NonNull;

import com.abatra.android.wheelie.logger.TimberInitializer;

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
    protected boolean isDebug() {
        return BuildConfig.DEBUG;
    }
}
