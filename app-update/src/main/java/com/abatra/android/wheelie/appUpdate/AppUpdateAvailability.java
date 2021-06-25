package com.abatra.android.wheelie.appUpdate;

public interface AppUpdateAvailability {

    boolean isUpdateAvailable();

    boolean isAppUpdateAllowedOfType(AppUpdateType appUpdateType);

    boolean isClientVersionOlderThanNDays(int days);
}
