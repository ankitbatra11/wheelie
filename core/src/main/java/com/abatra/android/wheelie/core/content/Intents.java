package com.abatra.android.wheelie.core.content;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import androidx.annotation.RequiresApi;

import com.google.common.base.MoreObjects;

import java.util.Optional;

import javax.annotation.Nullable;

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

    @SuppressLint("QueryPermissionsNeeded")
    public static boolean isLaunchable(@Nullable Intent intent, Context context) {
        return Optional.ofNullable(intent)
                .map(i -> i.resolveActivity(context.getPackageManager()) != null)
                .orElse(false);
    }

    public static String print(@Nullable Intent intent) {
        return Optional.ofNullable(intent)
                .map(Intents::printNotNull)
                .orElse("null");
    }

    private static String printNotNull(Intent intent) {
        return MoreObjects.toStringHelper(intent)
                .add("type", intent.getType())
                .add("action", intent.getAction())
                .add("data", intent.getData())
                .add("dataAuthority", getAuthority(intent).orElse(null))
                .add("extras", intent.getExtras())
                .add("flags", intent.getFlags())
                .omitNullValues()
                .toString();
    }

    private static Optional<String> getAuthority(Intent intent) {
        return Optional.ofNullable(intent.getData())
                .flatMap(uri -> Optional.ofNullable(uri.getAuthority()));
    }
}
