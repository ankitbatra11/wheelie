package com.abatra.android.wheelie.activity;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;

import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.abatra.android.wheelie.util.IntentUtils;

public final class ResultContracts {

    private ResultContracts() {
    }

    public static class SettingsScreen extends ActivityResultContract<Void, Void> {

        private final String action;

        private SettingsScreen(String action) {
            this.action = action;
        }

        public static SettingsScreen wirelessSettings() {
            return new SettingsScreen(Settings.ACTION_WIRELESS_SETTINGS);
        }

        @NonNull
        @Override
        public Intent createIntent(@NonNull Context context, Void input) {
            Intent intent = new Intent(action);
            if (!IntentUtils.isLaunchable(intent, context)) {
                intent = new Intent(Settings.ACTION_SETTINGS);
            }
            return intent;
        }

        @Override
        public Void parseResult(int resultCode, @Nullable Intent intent) {
            return null;
        }
    }
}
