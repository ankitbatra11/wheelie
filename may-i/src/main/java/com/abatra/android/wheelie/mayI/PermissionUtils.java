package com.abatra.android.wheelie.mayI;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;

import androidx.core.content.ContextCompat;

import java.util.Arrays;

public final class PermissionUtils {

    private PermissionUtils() {
    }

    public static boolean isPermissionGranted(Context context, String permission) {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean allPermissionsGranted(Context context, String[] permissions) {
        return Arrays.stream(permissions)
                .allMatch(p -> isPermissionGranted(context, p));
    }

    public static boolean canDrawOverlays(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return Settings.canDrawOverlays(context);
        }
        return true;
    }
}
