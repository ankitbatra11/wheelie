package com.abatra.android.wheelie.context;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

public class PackageManagerPackageRepository implements PackageRepository {

    private final Context context;

    public PackageManagerPackageRepository(Context context) {
        this.context = context;
    }

    @Override
    public String getVersionName(String packageName) {
        PackageInfo packageInfo;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException("package=" + packageName + " not found", e);
        }
        return packageInfo.versionName;
    }

    @Override
    public String getVersionName() {
        return getVersionName(context.getPackageName());
    }
}
