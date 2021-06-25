package com.abatra.android.wheelie.mayI;

import android.app.Activity;

import androidx.annotation.NonNull;

public class SinglePermissionGrantResult {

    static final SinglePermissionGrantResult GRANTED = new SinglePermissionGrantResult(true);
    static final SinglePermissionGrantResult DENIED = new SinglePermissionGrantResult(false);

    private final boolean permissionGranted;
    private boolean permissionPermanentlyDeniedBeforeRequest;

    SinglePermissionGrantResult(boolean permissionGranted) {
        this.permissionGranted = permissionGranted;
    }

    void setPermissionPermanentlyDeniedBeforeRequest(boolean permissionPermanentlyDeniedBeforeRequest) {
        this.permissionPermanentlyDeniedBeforeRequest = permissionPermanentlyDeniedBeforeRequest;
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

    interface Builder {

        void beforeRequestingPermission(String permission, Activity activity);

        SinglePermissionGrantResult onPermissionGrantResult(boolean grantResult, Activity activity);
    }
}
