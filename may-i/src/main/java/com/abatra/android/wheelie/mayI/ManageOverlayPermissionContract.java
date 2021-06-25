package com.abatra.android.wheelie.mayI;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ManageOverlayPermissionContract extends ActivityResultContract<String, Boolean> {

    private static final String PACKAGE_PREFIX = "package:";

    private final Context context;

    public ManageOverlayPermissionContract(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public Intent createIntent(@NonNull Context context, String input) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
                    .setData(createPackageUri(context));
        }
        return new Intent();
    }

    private static Uri createPackageUri(Context context) {
        return Uri.parse(PACKAGE_PREFIX + context.getPackageName());
    }

    @Override
    public Boolean parseResult(int resultCode, @Nullable Intent intent) {
        return PermissionUtils.canDrawOverlays(context);
    }
}
