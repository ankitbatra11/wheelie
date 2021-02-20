package com.abatra.android.wheelie.permission;

import androidx.activity.result.contract.ActivityResultContract;

import com.abatra.android.wheelie.activity.result.RequestManageOverlayPermission;

import javax.inject.Inject;

import static com.abatra.android.wheelie.permission.SinglePermissionGrantResult.Builder;

public class ManageOverlayPermissionRequestor extends AbstractSinglePermissionRequestor {

    @Inject
    public ManageOverlayPermissionRequestor() {
    }

    @Override
    protected ActivityResultContract<String, Boolean> createRequestPermissionActivityResultContract() {
        return new RequestManageOverlayPermission(getLifecycleOwner().getContext());
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
