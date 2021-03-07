package com.abatra.android.wheelie.update;

public interface AppUpdateAvailabilityChecker {

    void checkAppUpdateAvailability(AppUpdateCriteriaChecker appUpdateCriteriaChecker, Callback callback);

    interface Callback {

        void onAppUpdateAvailable(AppUpdateAvailability appUpdateAvailability);

        void onAppUpdateAvailableCheckFailed(Throwable error);

        void onAppUpdateCriteriaNotMet();
    }
}
