package com.abatra.android.wheelie.app;

import android.app.Application;

import com.abatra.android.wheelie.startup.ApplicationInitializers;

public class WheelieApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        getApplicationInitializers().initialize(this);
    }

    protected ApplicationInitializers getApplicationInitializers() {
        return ApplicationInitializers.EMPTY;
    }
}
