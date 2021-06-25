package com.abatra.android.wheelie.appUpdate;

public interface AppUpdateAvailabilityCriteria {

    AppUpdateAvailabilityCriteria IS_UPDATE_AVAILABLE = AppUpdateAvailability::isUpdateAvailable;

    static AppUpdateAvailabilityCriteria isAppUpdateAllowedOfType(AppUpdateType appUpdateType) {
        return appUpdateAvailability -> appUpdateAvailability.isAppUpdateAllowedOfType(appUpdateType);
    }

    boolean meets(AppUpdateAvailability appUpdateAvailability);

    default AppUpdateAvailabilityCriteria and(AppUpdateAvailabilityCriteria appUpdateAvailabilityCriteria) {
        return appUpdateAvailability -> AppUpdateAvailabilityCriteria.this.meets(appUpdateAvailability) &&
                appUpdateAvailabilityCriteria.meets(appUpdateAvailability);
    }

    default AppUpdateAvailabilityCriteria or(AppUpdateAvailabilityCriteria appUpdateAvailabilityCriteria) {
        return appUpdateAvailability -> AppUpdateAvailabilityCriteria.this.meets(appUpdateAvailability) ||
                appUpdateAvailabilityCriteria.meets(appUpdateAvailability);
    }
}
