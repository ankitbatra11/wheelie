package com.abatra.android.wheelie.permission;

import android.app.Activity;

import androidx.annotation.NonNull;

import static androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale;

public class SinglePermissionGrantResult {

    static final SinglePermissionGrantResult GRANTED = new SinglePermissionGrantResult(true);
    static final SinglePermissionGrantResult DENIED = new SinglePermissionGrantResult(false);

    private final boolean permissionGranted;
    private boolean permissionPermanentlyDeniedBeforeRequest;

    private SinglePermissionGrantResult(boolean permissionGranted) {
        this.permissionGranted = permissionGranted;
    }

    public boolean isPermissionGranted() {
        return permissionGranted;
    }

    public boolean isPermissionPermanentlyDeniedBeforeRequest() {
        return permissionPermanentlyDeniedBeforeRequest;
    }

    @NonNull
    @Override
    public String toString() {
        return "SinglePermissionGrantResult{" +
                "permissionGranted=" + permissionGranted +
                ", permissionPermanentlyDeniedBeforeRequest=" + permissionPermanentlyDeniedBeforeRequest +
                '}';
    }

    static class Builder {

        private final String permission;
        private boolean shouldShowRationaleBeforeRequest;

        Builder(String permission) {
            this.permission = permission;
        }

        void beforeRequestingPermission(Activity activity) {
            shouldShowRationaleBeforeRequest = shouldShowRequestPermissionRationale(activity, permission);
        }

        SinglePermissionGrantResult onPermissionGrantResult(Activity activity, boolean granted) {
            SinglePermissionGrantResult result;
            if (granted) {
                result = GRANTED;
            } else {
                boolean showRationaleAfterRequest = shouldShowRequestPermissionRationale(activity, permission);
                if (showRationaleAfterRequest) {
                    result = DENIED;
                } else {
                    result = new SinglePermissionGrantResult(false);
                    result.permissionPermanentlyDeniedBeforeRequest = !shouldShowRationaleBeforeRequest;
                }
            }
            return result;
        }
    }
}
