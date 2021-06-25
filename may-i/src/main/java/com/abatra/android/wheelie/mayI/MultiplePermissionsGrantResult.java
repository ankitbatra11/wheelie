package com.abatra.android.wheelie.mayI;

import android.app.Activity;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

public class MultiplePermissionsGrantResult {

    private final Map<String, SinglePermissionGrantResult> grantResultByPermission;

    MultiplePermissionsGrantResult(Map<String, SinglePermissionGrantResult> grantResultByPermission) {
        this.grantResultByPermission = grantResultByPermission;
    }

    static MultiplePermissionsGrantResult granted(String[] permissions) {
        Map<String, SinglePermissionGrantResult> map = new HashMap<>();
        for (String permission : permissions) {
            map.put(permission, SinglePermissionGrantResult.GRANTED);
        }
        return new MultiplePermissionsGrantResult(map);
    }

    public SinglePermissionGrantResult getSinglePermissionGrantResult(String permission) {
        return grantResultByPermission.get(permission);
    }

    @NonNull
    @Override
    public String toString() {
        return "MultiplePermissionsGrantResult{" +
                "grantResultByPermission=" + grantResultByPermission +
                '}';
    }

    static class Builder {

        private final Map<String, SinglePermissionGrantResult.Builder> buildersByPermission = new HashMap<>();

        void beforeRequestingPermissions(String[] permissions, Activity activity) {
            for (String permission : permissions) {
                SinglePermissionGrantResult.Builder builder = new ManifestSinglePermissionGrantResultBuilder();
                builder.beforeRequestingPermission(permission, activity);
                buildersByPermission.put(permission, builder);
            }
        }

        MultiplePermissionsGrantResult onMultiplePermissionsGrantResult(Map<String, Boolean> grantResult, Activity activity) {
            Map<String, SinglePermissionGrantResult> grantResultByPermission = new HashMap<>();
            for (Map.Entry<String, SinglePermissionGrantResult.Builder> builderByPermission : buildersByPermission.entrySet()) {
                boolean granted = grantResult.getOrDefault(builderByPermission.getKey(), false);
                SinglePermissionGrantResult value = builderByPermission.getValue().onPermissionGrantResult(granted, activity);
                grantResultByPermission.put(builderByPermission.getKey(), value);
            }
            return new MultiplePermissionsGrantResult(grantResultByPermission);
        }
    }
}
