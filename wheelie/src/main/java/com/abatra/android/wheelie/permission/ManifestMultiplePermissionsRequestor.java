package com.abatra.android.wheelie.permission;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts.RequestMultiplePermissions;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;

import com.abatra.android.wheelie.lifecycle.ILifecycleOwner;

import java.util.Optional;

public class ManifestMultiplePermissionsRequestor implements MultiplePermissionsRequestor {

    private ILifecycleOwner lifecycleOwner;
    private ActivityResultLauncher<String[]> multiplePermissionsRequestor;
    private CallbackDelegator callbackDelegator;

    @Override
    public void observeLifecycle(ILifecycleOwner lifecycleOwner) {
        this.lifecycleOwner = lifecycleOwner;
        this.lifecycleOwner.getLifecycle().addObserver(this);
    }

    @Override
    public void onCreate() {
        multiplePermissionsRequestor = lifecycleOwner.registerForActivityResult(new RequestMultiplePermissions(), result -> {
            Optional<CallbackDelegator> callbackDelegator = getCallbackDelegator();
            callbackDelegator.ifPresent(multiplePermissionsRequestCallbackDelegator -> {
                Optional<ILifecycleOwner> lifecycleOwner = getLifecycleOwner();
                lifecycleOwner.ifPresent(lo -> {
                    AppCompatActivity appCompatActivity = lo.getAppCompatActivity();
                    MultiplePermissionsGrantResult grantResult = this.callbackDelegator.builder.onMultiplePermissionsGrantResult(result, appCompatActivity);
                    this.callbackDelegator.onPermissionResult(grantResult);
                });
            });
        });
    }

    @VisibleForTesting
    Optional<ILifecycleOwner> getLifecycleOwner() {
        return Optional.ofNullable(lifecycleOwner);
    }

    @VisibleForTesting
    Optional<CallbackDelegator> getCallbackDelegator() {
        return Optional.ofNullable(callbackDelegator);
    }

    @Override
    public void requestSystemPermissions(String[] permissions, Callback callback) {
        if (PermissionUtils.allPermissionsGranted(lifecycleOwner.getContext(), permissions)) {
            callback.onPermissionResult(MultiplePermissionsGrantResult.granted(permissions));
        } else {
            MultiplePermissionsGrantResult.Builder builder = new MultiplePermissionsGrantResult.Builder();
            builder.beforeRequestingPermissions(permissions, lifecycleOwner.getAppCompatActivity());
            callbackDelegator = new CallbackDelegator(builder, callback);
            multiplePermissionsRequestor.launch(permissions);
        }
    }

    @Override
    public void onDestroy() {
        callbackDelegator = null;
        multiplePermissionsRequestor = null;
        lifecycleOwner = null;
    }

    /* Testing */

    ActivityResultLauncher<String[]> getMultiplePermissionsRequestor() {
        return multiplePermissionsRequestor;
    }

    private static class CallbackDelegator implements Callback {

        private final MultiplePermissionsGrantResult.Builder builder;
        private final Callback delegate;

        private CallbackDelegator(MultiplePermissionsGrantResult.Builder builder, Callback delegate) {
            this.builder = builder;
            this.delegate = delegate;
        }

        @Override
        public void onPermissionResult(MultiplePermissionsGrantResult multiplePermissionsGrantResult) {
            delegate.onPermissionResult(multiplePermissionsGrantResult);
        }
    }

}
