package com.abatra.android.wheelie.mayI;

import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission;

import static com.abatra.android.wheelie.mayI.SinglePermissionGrantResult.Builder;

public class ManifestSinglePermissionRequestor extends AbstractSinglePermissionRequestor {

    @Override
    protected boolean isPermissionGranted(String permission) {
        return getLifecycleOwner()
                .map(lifecycleOwner -> PermissionUtils.isPermissionGranted(lifecycleOwner.getContext(), permission))
                .orElse(false);
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
