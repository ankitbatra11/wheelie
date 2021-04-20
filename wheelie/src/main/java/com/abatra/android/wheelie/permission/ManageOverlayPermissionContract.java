package com.abatra.android.wheelie.permission;

import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.abatra.android.wheelie.intent.IntentFactory;
import com.abatra.android.wheelie.permission.PermissionUtils;

public class ManageOverlayPermissionContract extends ActivityResultContract<String, Boolean> {

    private final Context context;

    public ManageOverlayPermissionContract(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public Intent createIntent(@NonNull Context context, String input) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return IntentFactory.manageOverlayPermission(context);
        }
        return new Intent();
    }

    @Override
    public Boolean parseResult(int resultCode, @Nullable Intent intent) {
        return PermissionUtils.canDrawOverlays(context);
    }
}
