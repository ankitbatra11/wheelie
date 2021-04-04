package com.abatra.android.wheelie.update;

public interface AppUpdateAvailabilityChecker {

    /**
     * @param availabilityCriteria that needs to be met to get notified of an available update.
     * @param callback             to return check result.
     */
    void checkAppUpdateAvailability(AppUpdateAvailabilityCriteria availabilityCriteria, Callback callback);

    interface Callback {

        void onAppUpdateAvailable(AppUpdateAvailability updateAvailability);

        void onAppUpdateAvailableCheckFailed(Throwable error);

        void onAppUpdateCriteriaNotMet();
    }
}
