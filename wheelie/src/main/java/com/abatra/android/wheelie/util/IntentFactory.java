package com.abatra.android.wheelie.util;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;

public final class IntentFactory {

    private IntentFactory() {
    }

    public static Intent createWifiSettingsScreenIntent(Context context) {
        Intent result = new Intent(Settings.ACTION_WIFI_SETTINGS);
        if (!IntentUtils.isLaunchable(result, context)) {
            result = new Intent(Settings.ACTION_SETTINGS);
        }
        return result;
    }
}
