package com.abatra.android.wheelie.activity.result;

import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.abatra.android.wheelie.intent.IntentFactory;
import com.abatra.android.wheelie.permission.PermissionUtils;

@RequiresApi(api = Build.VERSION_CODES.M)
public class RequestManageOverlayPermission extends ActivityResultContract<String, Boolean> {

    private final Context context;

    public RequestManageOverlayPermission(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public Intent createIntent(@NonNull Context context, String input) {
        return IntentFactory.manageOverlayPermission(context);
    }

    @Override
    public Boolean parseResult(int resultCode, @Nullable Intent intent) {
        return PermissionUtils.canDrawOverlays(context);
    }
}
