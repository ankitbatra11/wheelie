package com.abatra.android.wheelie.update;

public interface AppUpdateAvailability {

    boolean isAppUpdateAllowedOfType(AppUpdateType appUpdateType);

    boolean isClientVersionOlderThanNDays(int days);
}
