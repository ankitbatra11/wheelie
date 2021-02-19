package com.abatra.android.wheelie.permission;

import android.content.ActivityNotFoundException;

import com.abatra.android.wheelie.lifecycle.ILifecycleObserver;

public interface MultiplePermissionsRequestor extends ILifecycleObserver {

    void requestSystemPermissions(String[] permissions, Callback callback) throws ActivityNotFoundException;

    interface Callback {
        default void onPermissionResult(MultiplePermissionsGrantResult multiplePermissionsGrantResult) {
        }
    }
}
