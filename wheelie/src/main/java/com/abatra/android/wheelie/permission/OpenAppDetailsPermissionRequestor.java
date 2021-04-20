package com.abatra.android.wheelie.permission;

import android.content.ActivityNotFoundException;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.Nullable;

import com.abatra.android.wheelie.activity.ActivityStarter;
import com.abatra.android.wheelie.activity.result.contract.OpenAppDetailsContract;
import com.abatra.android.wheelie.lifecycle.owner.ILifecycleOwner;

import java.util.Arrays;
import java.util.Optional;

public class OpenAppDetailsPermissionRequestor implements PermissionRequestor {

    private final PermissionRequestor permissionRequestor;
    private final ActivityStarter activityStarter;
    private ILifecycleOwner lifecycleOwner;
    private ActivityResultLauncher<Void> appDetailsScreenLauncher;
    @Nullable
    private ActivityResultCallback<ActivityResult> appDetailsScreenResultCallback;

    public OpenAppDetailsPermissionRequestor(PermissionRequestor permissionRequestor, ActivityStarter activityStarter) {
        this.permissionRequestor = permissionRequestor;
        this.activityStarter = activityStarter;
    }

    @Override
    public void observeLifecycle(ILifecycleOwner lifecycleOwner) {

        this.lifecycleOwner = lifecycleOwner;
        this.lifecycleOwner.getLifecycle().addObserver(this);

        permissionRequestor.observeLifecycle(this.lifecycleOwner);

        appDetailsScreenLauncher = this.lifecycleOwner.registerForActivityResult(new OpenAppDetailsContract(), result -> {
            Optional<ActivityResultCallback<ActivityResult>> resultCallback = Optional.ofNullable(this.appDetailsScreenResultCallback);
            resultCallback.ifPresent(callback -> callback.onActivityResult(result));
        });
    }

    @Override
    public void requestSystemPermissions(String[] permissions, MultiplePermissionsRequestor.Callback callback) throws ActivityNotFoundException {
        try {
            permissionRequestor.requestSystemPermissions(permissions, new MultiplePermissionsRequestor.Callback() {
                @Override
                public void onPermissionResult(MultiplePermissionsGrantResult multiplePermissionsGrantResult) {
                    if (anyPermissionPermanentlyDeniedBefore(permissions, multiplePermissionsGrantResult)) {
                        appDetailsScreenResultCallback = result -> permissionRequestor.requestSystemPermissions(permissions, callback);
                        openAppDetailsScreen();
                    } else {
                        callback.onPermissionResult(multiplePermissionsGrantResult);
                    }
                }
            });
        } catch (ActivityNotFoundException e) {
            openAppDetailsScreen();
        }
    }

    private boolean anyPermissionPermanentlyDeniedBefore(String[] permissions,
                                                         MultiplePermissionsGrantResult multiplePermissionsGrantResult) {
        return Arrays.stream(permissions)
                .map(multiplePermissionsGrantResult::getSinglePermissionGrantResult)
                .anyMatch(SinglePermissionGrantResult::isPermissionPermanentlyDeniedBeforeRequest);
    }

    private void openAppDetailsScreen() {
        activityStarter.launch(appDetailsScreenLauncher, null);
    }

    @Override
    public void requestSystemPermission(String permission, SinglePermissionRequestor.Callback callback) throws ActivityNotFoundException {
        try {
            permissionRequestor.requestSystemPermission(permission, grantResult -> {
                if (grantResult.isPermissionPermanentlyDeniedBeforeRequest()) {
                    appDetailsScreenResultCallback = result -> permissionRequestor.requestSystemPermission(permission, callback);
                    openAppDetailsScreen();
                } else {
                    callback.onPermissionResult(grantResult);
                }
            });
        } catch (ActivityNotFoundException e) {
            openAppDetailsScreen();
        }
    }

    @Override
    public void onDestroy() {
        lifecycleOwner = null;
    }
}
