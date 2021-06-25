package com.abatra.android.wheelie.demo;

import com.abatra.android.wheelie.core.debug.StrictModeInitializer;

public class DebugStrictModeInitializer extends StrictModeInitializer {

    @Override
    protected boolean isDebug() {
        return BuildConfig.DEBUG;
    }
}
