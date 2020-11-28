package com.abatra.android.wheelie.demo;

import android.app.Application;

import timber.log.Timber;

public class WheelieApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Timber.plant(new Timber.DebugTree());
    }
}
