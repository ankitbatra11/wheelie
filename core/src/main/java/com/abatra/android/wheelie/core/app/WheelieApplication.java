package com.abatra.android.wheelie.core.app;

import android.app.Application;

import com.abatra.android.wheelie.core.startup.ApplicationInitializers;

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
