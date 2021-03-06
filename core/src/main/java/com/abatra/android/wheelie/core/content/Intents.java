package com.abatra.android.wheelie.core.content;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import androidx.annotation.RequiresApi;

public final class Intents {

    private static final String PACKAGE_PREFIX = "package:";

    private Intents() {
    }

    public static Intent openAppDetailsSettings(Context context) {
        return new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                .setData(createPackageUri(context));
    }

    private static Uri createPackageUri(Context context) {
        return Uri.parse(PACKAGE_PREFIX + context.getPackageName());
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static Intent manageOverlayPermission(Context context) {
        return new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
                .setData(createPackageUri(context));
    }

    public static boolean isLaunchable(Intent intent, Context context) {
        return intent.resolveActivity(context.getPackageManager()) != null;
    }
}
