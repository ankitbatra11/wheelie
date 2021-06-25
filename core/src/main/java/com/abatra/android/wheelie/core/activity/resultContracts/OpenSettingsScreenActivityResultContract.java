package com.abatra.android.wheelie.core.activity.resultContracts;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;

import androidx.annotation.NonNull;

import com.abatra.android.wheelie.core.content.Intents;

public class OpenSettingsScreenActivityResultContract extends AbstractActivityResultContract<Void> {

    private final String action;

    private OpenSettingsScreenActivityResultContract(String action) {
        this.action = action;
    }

    public static OpenSettingsScreenActivityResultContract wirelessSettings() {
        return new OpenSettingsScreenActivityResultContract(Settings.ACTION_WIRELESS_SETTINGS);
    }

    @NonNull
    @Override
    public Intent createIntent(@NonNull Context context, Void input) {
        Intent intent = new Intent(action);
        if (!Intents.isLaunchable(intent, context)) {
            intent = new Intent(Settings.ACTION_SETTINGS);
        }
        return intent;
    }
}
