package com.abatra.android.wheelie.permission;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;

import com.abatra.android.wheelie.lifecycle.ILifecycleOwner;

import java.util.Optional;

import static com.abatra.android.wheelie.permission.SinglePermissionGrantResult.Builder;
import static com.abatra.android.wheelie.permission.SinglePermissionGrantResult.GRANTED;

public class ManifestSinglePermissionRequestor implements SinglePermissionRequestor {

    private ILifecycleOwner lifecycleOwner;
    private ActivityResultLauncher<String> singlePermissionRequestor;
    private CallbackDelegator callbackDelegator;

    @Override
    public void observeLifecycle(ILifecycleOwner lifecycleOwner) {
        this.lifecycleOwner = lifecycleOwner;
        this.lifecycleOwner.getLifecycle().addObserver(this);
    }

    @Override
    public void onCreate() {
        ActivityResultCallback<Boolean> activityResultCallback = result -> {
            Optional<CallbackDelegator> requestCallback = getCallbackDelegator();
            requestCallback.ifPresent(callback -> {
                Optional<ILifecycleOwner> ownerOptional = getLifecycleOwner();
                ownerOptional.ifPresent(lo -> {
                    AppCompatActivity appCompatActivity = lo.getAppCompatActivity();
                    callback.onPermissionResult(callback.builder.onPermissionGrantResult(appCompatActivity, result));
                });
            });
        };
        singlePermissionRequestor = lifecycleOwner.registerForActivityResult(new RequestPermission(), activityResultCallback);
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
    public void requestSystemPermission(String permission, Callback callback) {
        if (PermissionUtils.isPermissionGranted(lifecycleOwner.getContext(), permission)) {
            callback.onPermissionResult(GRANTED);
        } else {
            SinglePermissionGrantResult.Builder builder = new Builder(permission);
            builder.beforeRequestingPermission(lifecycleOwner.getAppCompatActivity());
            callbackDelegator = new CallbackDelegator(builder, callback);
            singlePermissionRequestor.launch(permission);
        }
    }

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

        private final SinglePermissionGrantResult.Builder builder;
        private final Callback delegate;

        private CallbackDelegator(SinglePermissionGrantResult.Builder builder, Callback delegate) {
            this.builder = builder;
            this.delegate = delegate;
        }

        @Override
        public void onPermissionResult(SinglePermissionGrantResult grantResult) {
            delegate.onPermissionResult(grantResult);
        }
    }

}
