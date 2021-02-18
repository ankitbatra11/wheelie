package com.abatra.android.wheelie.permission;

import com.abatra.android.wheelie.lifecycle.ILifecycleObserver;

public interface PermissionRequestor extends ILifecycleObserver {

    void requestSystemPermission(String permission, SinglePermissionRequestCallback singlePermissionRequestCallback);

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
}
