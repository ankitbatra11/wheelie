package com.abatra.android.wheelie.permission;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;

import com.abatra.android.wheelie.lifecycle.owner.ILifecycleOwner;

import java.util.Optional;

import static com.abatra.android.wheelie.permission.SinglePermissionGrantResult.Builder;
import static com.abatra.android.wheelie.permission.SinglePermissionGrantResult.GRANTED;

abstract class AbstractSinglePermissionRequestor implements SinglePermissionRequestor {

    private ILifecycleOwner lifecycleOwner;
    private ActivityResultLauncher<String> singlePermissionRequestor;
    private CallbackDelegator callbackDelegator;

    @Override
    public void observeLifecycle(ILifecycleOwner lifecycleOwner) {

        this.lifecycleOwner = lifecycleOwner;
        this.lifecycleOwner.getLifecycle().addObserver(this);

        ActivityResultCallback<Boolean> activityResultCallback = result -> {
            Optional<CallbackDelegator> requestCallback = getCallbackDelegator();
            requestCallback.ifPresent(callback -> {
                Optional<ILifecycleOwner> ownerOptional = getLifecycleOwnerOptional();
                ownerOptional.ifPresent(lo -> {
                    AppCompatActivity appCompatActivity = lo.getAppCompatActivity();
                    callback.onPermissionResult(callback.builder.onPermissionGrantResult(result, appCompatActivity));
                });
            });
        };
        singlePermissionRequestor = lifecycleOwner.registerForActivityResult(
                createRequestPermissionActivityResultContract(),
                activityResultCallback);
    }

    protected abstract ActivityResultContract<String, Boolean> createRequestPermissionActivityResultContract();

    protected Optional<ILifecycleOwner> getLifecycleOwnerOptional() {
        return Optional.ofNullable(getLifecycleOwner());
    }

    protected ILifecycleOwner getLifecycleOwner() {
        return lifecycleOwner;
    }

    @VisibleForTesting
    Optional<CallbackDelegator> getCallbackDelegator() {
        return Optional.ofNullable(callbackDelegator);
    }

    @Override
    public void requestSystemPermission(String permission, Callback callback) {
        if (isPermissionGranted(permission)) {
            callback.onPermissionResult(GRANTED);
        } else {
            Builder builder = createBuilder();
            builder.beforeRequestingPermission(permission, lifecycleOwner.getAppCompatActivity());
            callbackDelegator = new CallbackDelegator(builder, callback);
            singlePermissionRequestor.launch(permission);
        }
    }

    protected abstract boolean isPermissionGranted(String permission);

    protected abstract SinglePermissionGrantResult.Builder createBuilder();

    @Override
    public void onDestroy() {
        callbackDelegator = null;
        singlePermissionRequestor = null;
        lifecycleOwner = null;
    }

    /* Testing */

    ActivityResultLauncher<String> getSinglePermissionRequestor() {
        return singlePermissionRequestor;
    }

    private static class CallbackDelegator implements Callback {

        private final Builder builder;
        private final Callback delegate;

        private CallbackDelegator(Builder builder, Callback delegate) {
            this.builder = builder;
            this.delegate = delegate;
        }

        @Override
        public void onPermissionResult(SinglePermissionGrantResult grantResult) {
            delegate.onPermissionResult(grantResult);
        }
    }

}
