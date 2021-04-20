package com.abatra.android.wheelie.context;

public interface PackageRepository {

    String getVersionName(String packageName);

    /**
     * @return The version name of the app.
     */
    String getVersionName();
}
