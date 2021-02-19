package com.abatra.android.wheelie.intent;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;

public final class IntentFactory {

    private static final String PACKAGE_PREFIX = "package:";

    private IntentFactory() {
    }

    public static Intent openAppDetailsSettings(Context context) {
        return new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                .setData(Uri.parse(PACKAGE_PREFIX + context.getPackageName()));
    }
}
