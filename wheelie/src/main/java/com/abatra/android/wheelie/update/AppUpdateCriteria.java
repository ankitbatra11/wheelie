package com.abatra.android.wheelie.update;

public interface AppUpdateCriteria {

    AppUpdateCriteria IS_UPDATE_AVAILABLE = AppUpdateAvailability::isUpdateAvailable;

    boolean meets(AppUpdateAvailability appUpdateAvailability);

    default AppUpdateCriteria and(AppUpdateCriteria appUpdateCriteria) {
        return appUpdateAvailability -> AppUpdateCriteria.this.meets(appUpdateAvailability) &&
                appUpdateCriteria.meets(appUpdateAvailability);
    }
}
