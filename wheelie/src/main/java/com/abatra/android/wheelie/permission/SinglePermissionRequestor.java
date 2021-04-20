package com.abatra.android.wheelie.permission;

import android.content.ActivityNotFoundException;

import com.abatra.android.wheelie.lifecycle.observer.ILifecycleObserver;

public interface SinglePermissionRequestor extends ILifecycleObserver {

    void requestSystemPermission(String permission, Callback callback) throws ActivityNotFoundException;

    interface Callback {
        void onPermissionResult(SinglePermissionGrantResult grantResult);
    }
}
