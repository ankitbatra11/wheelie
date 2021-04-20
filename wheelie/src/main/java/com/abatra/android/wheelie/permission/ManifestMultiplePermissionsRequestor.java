package com.abatra.android.wheelie.permission;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts.RequestMultiplePermissions;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;

import com.abatra.android.wheelie.lifecycle.owner.ILifecycleOwner;

import java.util.Optional;

public class ManifestMultiplePermissionsRequestor implements MultiplePermissionsRequestor {

    private ILifecycleOwner lifecycleOwner;
    private ActivityResultLauncher<String[]> multiplePermissionsActivityResultLauncher;
    private CallbackDelegator callbackDelegator;

    @Override
    public void observeLifecycle(ILifecycleOwner lifecycleOwner) {

        this.lifecycleOwner = lifecycleOwner;
        this.lifecycleOwner.getLifecycle().addObserver(this);

        RequestMultiplePermissions contract = new RequestMultiplePermissions();
        multiplePermissionsActivityResultLauncher = lifecycleOwner.registerForActivityResult(contract, result -> {
            Optional<CallbackDelegator> callbackDelegator = getCallbackDelegator();
            callbackDelegator.ifPresent(multiplePermissionsRequestCallbackDelegator -> {
                Optional<ILifecycleOwner> lifecycleOwnerOptional = getLifecycleOwner();
                lifecycleOwnerOptional.ifPresent(lo -> {
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
            multiplePermissionsActivityResultLauncher.launch(permissions);
        }
    }

    @Override
    public void onDestroy() {
        callbackDelegator = null;
        multiplePermissionsActivityResultLauncher = null;
        lifecycleOwner = null;
    }

    /* Testing */

    ActivityResultLauncher<String[]> getMultiplePermissionsActivityResultLauncher() {
        return multiplePermissionsActivityResultLauncher;
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
