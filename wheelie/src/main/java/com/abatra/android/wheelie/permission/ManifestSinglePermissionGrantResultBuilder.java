package com.abatra.android.wheelie.permission;

import android.app.Activity;

import static androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale;

class ManifestSinglePermissionGrantResultBuilder implements SinglePermissionGrantResult.Builder {

    private String permission;
    private boolean shouldShowRationaleBeforeRequest;

    @Override
    public void beforeRequestingPermission(String permission, Activity activity) {
        this.permission = permission;
        shouldShowRationaleBeforeRequest = shouldShowRequestPermissionRationale(activity, permission);
    }

    @Override
    public SinglePermissionGrantResult onPermissionGrantResult(boolean grantResult, Activity activity) {
        SinglePermissionGrantResult result;
        if (grantResult) {
            result = SinglePermissionGrantResult.GRANTED;
        } else {
            boolean showRationaleAfterRequest = shouldShowRequestPermissionRationale(activity, permission);
            if (showRationaleAfterRequest) {
                result = SinglePermissionGrantResult.DENIED;
            } else {
                result = new SinglePermissionGrantResult(false);
                result.setPermissionPermanentlyDeniedBeforeRequest(!shouldShowRationaleBeforeRequest);
            }
        }
        return result;
    }
}
