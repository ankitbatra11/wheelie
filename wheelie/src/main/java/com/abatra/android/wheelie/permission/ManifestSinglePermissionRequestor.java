package com.abatra.android.wheelie.permission;

import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission;

import javax.inject.Inject;

import static com.abatra.android.wheelie.permission.SinglePermissionGrantResult.Builder;

public class ManifestSinglePermissionRequestor extends AbstractSinglePermissionRequestor {

    @Inject
    public ManifestSinglePermissionRequestor() {
    }

    @Override
    protected boolean isPermissionGranted(String permission) {
        return PermissionUtils.isPermissionGranted(getLifecycleOwner().getContext(), permission);
    }

    @Override
    protected ActivityResultContract<String, Boolean> createRequestPermissionActivityResultContract() {
        return new RequestPermission();
    }

    @Override
    protected Builder createBuilder() {
        return new ManifestSinglePermissionGrantResultBuilder();
    }
}
