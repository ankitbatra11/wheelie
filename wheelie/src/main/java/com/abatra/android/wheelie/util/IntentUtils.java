package com.abatra.android.wheelie.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;

public final class IntentUtils {

    private IntentUtils() {
    }

    @SuppressLint("QueryPermissionsNeeded")
    public static boolean isLaunchable(Intent intent, Context context) {
        return intent.resolveActivity(context.getPackageManager()) != null;
    }
}
