package com.abatra.android.wheelie.permission;

import android.os.Build;

import androidx.activity.result.contract.ActivityResultContract;

import com.abatra.android.wheelie.activity.result.RequestManageOverlayPermission;

import static com.abatra.android.wheelie.permission.SinglePermissionGrantResult.Builder;

public class ManageOverlayPermissionRequestor extends AbstractSinglePermissionRequestor {

    @Override
    protected ActivityResultContract<String, Boolean> createRequestPermissionActivityResultContract() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return new RequestManageOverlayPermission(getLifecycleOwner().getContext());
        }
        throw new IllegalStateException("isPermissionGranted should have returned true for Marshmallow or below!");
    }

    @Override
    protected boolean isPermissionGranted(String permission) {
        return PermissionUtils.canDrawOverlays(getLifecycleOwner().getContext());
    }

    @Override
    protected Builder createBuilder() {
        return ManageOverlayPermissionGrantResultBuilder.INSTANCE;
    }
}
