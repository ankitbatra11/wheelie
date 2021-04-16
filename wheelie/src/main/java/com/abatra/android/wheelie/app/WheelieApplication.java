package com.abatra.android.wheelie.app;

import android.app.Application;

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
