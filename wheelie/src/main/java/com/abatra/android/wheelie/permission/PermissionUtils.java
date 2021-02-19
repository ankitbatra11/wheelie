package com.abatra.android.wheelie.permission;

import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.content.ContextCompat;

import java.util.Arrays;

public final class PermissionUtils {

    private PermissionUtils() {
    }

    public static boolean isPermissionGranted(Context context, String permission) {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean allGranted(Context context, String[] permissions) {
        return Arrays.stream(permissions)
                .allMatch(p -> isPermissionGranted(context, p));
    }
}
