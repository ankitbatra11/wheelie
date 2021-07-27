package com.abatra.android.wheelie.mayI;

import androidx.activity.result.contract.ActivityResultContract;

import static com.abatra.android.wheelie.mayI.PermissionUtils.*;
import static com.abatra.android.wheelie.mayI.SinglePermissionGrantResult.Builder;

public class ManageOverlayPermissionRequestor extends AbstractSinglePermissionRequestor {

    @Override
    protected ActivityResultContract<String, Boolean> createRequestPermissionActivityResultContract() {
        return new ManageOverlayPermissionContract(requireLifecycleOwner().getContext());
    }

    @Override
    protected boolean isPermissionGranted(String permission) {
        return getLifecycleOwner()
                .map(lifecycleOwner -> canDrawOverlays(lifecycleOwner.getContext()))
                .orElse(false);
    }

    @Override
    protected Builder createBuilder() {
        return ManageOverlayPermissionGrantResultBuilder.INSTANCE;
    }
}
