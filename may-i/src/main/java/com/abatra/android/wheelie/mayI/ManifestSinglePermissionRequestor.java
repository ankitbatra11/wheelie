package com.abatra.android.wheelie.mayI;

import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission;

import static com.abatra.android.wheelie.mayI.SinglePermissionGrantResult.Builder;

public class ManifestSinglePermissionRequestor extends AbstractSinglePermissionRequestor {

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
