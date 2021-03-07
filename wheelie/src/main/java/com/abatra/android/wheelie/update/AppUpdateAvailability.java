package com.abatra.android.wheelie.update;

public interface AppUpdateAvailability {

    boolean isUpdateAvailable();

    boolean isAppUpdateAllowedOfType(AppUpdateType appUpdateType);

    boolean isClientVersionOlderThanNDays(int days);
}
