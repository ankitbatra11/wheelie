package com.abatra.android.wheelie.permission;

import android.content.ActivityNotFoundException;

import com.abatra.android.wheelie.lifecycle.owner.ILifecycleOwner;

public class HybridPermissionRequestor implements PermissionRequestor {

    private final SinglePermissionRequestor singlePermissionRequestor;
    private final MultiplePermissionsRequestor multiplePermissionsRequestor;

    public HybridPermissionRequestor(SinglePermissionRequestor singlePermissionRequestor,
                                     MultiplePermissionsRequestor multiplePermissionsRequestor) {
        this.singlePermissionRequestor = singlePermissionRequestor;
        this.multiplePermissionsRequestor = multiplePermissionsRequestor;
    }

    @Override
    public void observeLifecycle(ILifecycleOwner lifecycleOwner) {
        singlePermissionRequestor.observeLifecycle(lifecycleOwner);
        multiplePermissionsRequestor.observeLifecycle(lifecycleOwner);
    }

    @Override
    public void requestSystemPermissions(String[] permissions, MultiplePermissionsRequestor.Callback callback) throws ActivityNotFoundException {
        multiplePermissionsRequestor.requestSystemPermissions(permissions, callback);
    }

    @Override
    public void requestSystemPermission(String permission, SinglePermissionRequestor.Callback callback) throws ActivityNotFoundException {
        singlePermissionRequestor.requestSystemPermission(permission, callback);
    }
}
