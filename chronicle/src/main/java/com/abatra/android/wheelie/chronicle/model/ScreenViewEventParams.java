package com.abatra.android.wheelie.chronicle.model;

import android.app.Activity;

import androidx.fragment.app.Fragment;

public class ScreenViewEventParams {

    private final String screenClass;
    private final String screenName;

    public ScreenViewEventParams(String screenClass, String screenName) {
        this.screenClass = screenClass;
        this.screenName = screenName;
    }

    public static ScreenViewEventParams from(Fragment fragment) {
        String fragmentClassName = fragment.getClass().getSimpleName();
        return new ScreenViewEventParams(fragmentClassName, fragmentClassName);
    }

    public static ScreenViewEventParams from(Activity activity) {
        String activityClassName = activity.getClass().getSimpleName();
        return new ScreenViewEventParams(activityClassName, activityClassName);
    }

    public String getScreenClass() {
        return screenClass;
    }

    public String getScreenName() {
        return screenName;
    }
}
