package com.abatra.android.wheelie.core.remoteConfig;

public interface DynamicConfig {

    void initialize(DynamicConfigSettings settings);

    String getStringValue(String key);

    long getLongValue(String key);

    void fetchAndActivate();
}
