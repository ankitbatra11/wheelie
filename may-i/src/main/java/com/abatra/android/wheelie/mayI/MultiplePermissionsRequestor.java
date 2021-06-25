package com.abatra.android.wheelie.mayI;

import android.content.ActivityNotFoundException;

import com.abatra.android.wheelie.lifecycle.observer.ILifecycleObserver;

public interface MultiplePermissionsRequestor extends ILifecycleObserver {

    void requestSystemPermissions(String[] permissions, Callback callback) throws ActivityNotFoundException;

    interface Callback {
        default void onPermissionResult(MultiplePermissionsGrantResult multiplePermissionsGrantResult) {
        }
    }
}
