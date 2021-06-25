package com.abatra.android.wheelie.mayI;

import android.app.Activity;

class ManageOverlayPermissionGrantResultBuilder implements SinglePermissionGrantResult.Builder {

    static final ManageOverlayPermissionGrantResultBuilder INSTANCE = new ManageOverlayPermissionGrantResultBuilder();

    private ManageOverlayPermissionGrantResultBuilder() {
    }

    @Override
    public void beforeRequestingPermission(String permission, Activity activity) {
    }

    @Override
    public SinglePermissionGrantResult onPermissionGrantResult(boolean grantResult, Activity activity) {
        return PermissionUtils.canDrawOverlays(activity)
                ? SinglePermissionGrantResult.GRANTED
                : SinglePermissionGrantResult.DENIED;
    }
}
