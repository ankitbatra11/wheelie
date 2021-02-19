package com.abatra.android.wheelie.permission;

import android.content.ActivityNotFoundException;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.activity.result.contract.ActivityResultContracts.RequestMultiplePermissions;
import androidx.annotation.VisibleForTesting;

import com.abatra.android.wheelie.lifecycle.ILifecycleOwner;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale;

public class ActivityResultApiPermissionRequestor implements PermissionRequestor {

    private ILifecycleOwner lifecycleOwner;

    private ActivityResultLauncher<String> singlePermissionRequestor;
    private SinglePermissionRequestCallbackDelegator singlePermissionRequestCallbackDelegator;

    private ActivityResultLauncher<String[]> multiplePermissionsRequestor;
    private MultiplePermissionsRequestCallback multiplePermissionsRequestCallback;

    @Override
    public void observeLifecycle(ILifecycleOwner lifecycleOwner) {
        this.lifecycleOwner = lifecycleOwner;
        this.lifecycleOwner.getLifecycle().addObserver(this);
    }

    @Override
    public void onCreate() {
        ActivityResultCallback<Boolean> singlePermissionActivityResultCallback = result -> {
            Optional<SinglePermissionRequestCallbackDelegator> requestCallback = getSinglePermissionRequestCallbackDelegator();
            requestCallback.ifPresent(callback -> {
                if (result) {
                    callback.onPermissionGranted();
                } else {
                    Optional<ILifecycleOwner> ownerOptional = getLifecycleOwner();
                    ownerOptional.ifPresent(lo -> {
                        boolean showRationaleAfterRequest = shouldShowRequestPermissionRationale(lo.getAppCompatActivity(), callback.getPermission());
                        if (showRationaleAfterRequest) {
                            callback.onPermissionDenied();
                        } else {
                            callback.onPermissionPermanentlyDenied(callback.shouldShowPermissionRationaleBeforeRequest());
                        }
                    });
                }
            });
        };
        singlePermissionRequestor = lifecycleOwner.registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                singlePermissionActivityResultCallback);

        multiplePermissionsRequestor = lifecycleOwner.registerForActivityResult(new RequestMultiplePermissions(), result -> {
            Optional<MultiplePermissionsRequestCallback> callback = getMultiplePermissionsRequestCallback();
            callback.ifPresent(c -> c.onPermissionResult(result));
        });
    }

    @VisibleForTesting
    Optional<ILifecycleOwner> getLifecycleOwner() {
        return Optional.ofNullable(lifecycleOwner);
    }

    @VisibleForTesting
    Optional<SinglePermissionRequestCallbackDelegator> getSinglePermissionRequestCallbackDelegator() {
        return Optional.ofNullable(singlePermissionRequestCallbackDelegator);
    }

    @VisibleForTesting
    Optional<MultiplePermissionsRequestCallback> getMultiplePermissionsRequestCallback() {
        return Optional.ofNullable(multiplePermissionsRequestCallback);
    }

    @Override
    public void requestSystemPermission(String permission,
                                        SinglePermissionRequestCallback singlePermissionRequestCallback) {
        if (PermissionUtils.isPermissionGranted(lifecycleOwner.getContext(), permission)) {
            singlePermissionRequestCallback.onPermissionGranted();
        } else {
            singlePermissionRequestCallbackDelegator = new SinglePermissionRequestCallbackDelegator(permission,
                    singlePermissionRequestCallback,
                    shouldShowRequestPermissionRationale(lifecycleOwner.getAppCompatActivity(), permission));
            try {
                singlePermissionRequestor.launch(permission);
            } catch (ActivityNotFoundException e) {
                singlePermissionRequestCallbackDelegator.onPermissionHandlerActivityNotFound();
            }
        }
    }

    @Override
    public void requestSystemPermissions(String[] permissions,
                                         MultiplePermissionsRequestCallback multiplePermissionsRequestCallback) {

        this.multiplePermissionsRequestCallback = multiplePermissionsRequestCallback;

        if (PermissionUtils.allGranted(lifecycleOwner.getContext(), permissions)) {
            this.multiplePermissionsRequestCallback.onPermissionResult(createAllGrantedResult(permissions));
        } else {
            this.multiplePermissionsRequestCallback = multiplePermissionsRequestCallback;
            try {
                multiplePermissionsRequestor.launch(permissions);
            } catch (ActivityNotFoundException e) {
                this.multiplePermissionsRequestCallback.onPermissionHandlerActivityNotFound();
            }
        }
    }

    private Map<String, Boolean> createAllGrantedResult(String[] permissions) {
        Map<String, Boolean> result = new HashMap<>();
        for (String permission : permissions) {
            result.put(permission, true);
        }
        return result;
    }

    @Override
    public void onDestroy() {

        multiplePermissionsRequestCallback = null;
        multiplePermissionsRequestor = null;

        singlePermissionRequestCallbackDelegator = null;
        singlePermissionRequestor = null;

        lifecycleOwner = null;
    }

    /* Testing */

    ActivityResultLauncher<String> getSinglePermissionRequestor() {
        return singlePermissionRequestor;
    }

    ActivityResultLauncher<String[]> getMultiplePermissionsRequestor() {
        return multiplePermissionsRequestor;
    }

    private static class SinglePermissionRequestCallbackDelegator implements SinglePermissionRequestCallback {

        private final String permission;
        private final SinglePermissionRequestCallback delegate;
        private final boolean shouldShowPermissionRationaleBeforeRequest;

        private SinglePermissionRequestCallbackDelegator(String permission,
                                                         SinglePermissionRequestCallback delegate,
                                                         boolean shouldShowPermissionRationaleBeforeRequest) {
            this.permission = permission;
            this.delegate = delegate;
            this.shouldShowPermissionRationaleBeforeRequest = shouldShowPermissionRationaleBeforeRequest;
        }

        @Override
        public void onPermissionGranted() {
            delegate.onPermissionGranted();
        }

        @Override
        public void onPermissionDenied() {
            delegate.onPermissionDenied();
        }

        @Override
        public void onPermissionHandlerActivityNotFound() {
            delegate.onPermissionHandlerActivityNotFound();
        }

        @Override
        public void onPermissionPermanentlyDenied(boolean shouldShowPermissionRationaleBeforeRequest) {
            delegate.onPermissionPermanentlyDenied(shouldShowPermissionRationaleBeforeRequest);
        }

        public String getPermission() {
            return permission;
        }

        public boolean shouldShowPermissionRationaleBeforeRequest() {
            return shouldShowPermissionRationaleBeforeRequest;
        }
    }

}
