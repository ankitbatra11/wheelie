package com.abatra.android.wheelie.permission;

import com.abatra.android.wheelie.lifecycle.ILifecycleObserver;

import java.util.Map;

public interface PermissionRequestor extends ILifecycleObserver {

    void requestSystemPermission(String permission, SinglePermissionRequestCallback singlePermissionRequestCallback);

    void requestSystemPermissions(String[] permissions, MultiplePermissionsRequestCallback multiplePermissionsRequestCallback);

    interface SinglePermissionRequestCallback {

        default void onPermissionGranted() {
        }

        default void onPermissionDenied() {
        }

        default void onPermissionHandlerActivityNotFound() {
        }

        default void onPermissionPermanentlyDenied() {
        }
    }

    interface MultiplePermissionsRequestCallback {

        /**
         * @param grantResultByPermission Map of permission grant result by permission. May not include a permission
         *                                in the result map if that permission has been permanently denied.
         */
        default void onPermissionResult(Map<String, Boolean> grantResultByPermission) {
        }

        default void onPermissionHandlerActivityNotFound() {
        }
    }
}
