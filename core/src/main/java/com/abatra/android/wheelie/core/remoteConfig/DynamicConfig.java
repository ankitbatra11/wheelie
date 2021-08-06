package com.abatra.android.wheelie.core.remoteConfig;

import com.abatra.android.wheelie.core.startup.ApplicationInitializer;

public interface DynamicConfig extends ApplicationInitializer {

    void initialize(DynamicConfigSettings settings);

    String getStringValue(String key);

    long getLongValue(String key);

    void fetchAndActivate();
}
