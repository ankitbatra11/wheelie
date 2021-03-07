package com.abatra.android.wheelie.update.playstore;

import com.abatra.android.wheelie.update.AppUpdateAvailabilityChecker;
import com.abatra.android.wheelie.update.AppUpdateCriteriaChecker;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;

import java.util.Optional;

import timber.log.Timber;

public class PlayStoreAppUpdateAvailabilityChecker implements AppUpdateAvailabilityChecker {

    private final AppUpdateManager appUpdateManager;

    public PlayStoreAppUpdateAvailabilityChecker(AppUpdateManager appUpdateManager) {
        this.appUpdateManager = appUpdateManager;
    }

    @Override
    public void checkAppUpdateAvailability(AppUpdateCriteriaChecker appUpdateCriteriaChecker, Callback callback) {
        appUpdateManager.getAppUpdateInfo().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                onGetAppUpdateInfoSuccess(task.getResult(), appUpdateCriteriaChecker, callback);
            } else {
                Timber.e(task.getException(), "App update availability check failed!");
                callback.onAppUpdateAvailableCheckFailed(task.getException());
            }
        });
    }

    private void onGetAppUpdateInfoSuccess(AppUpdateInfo appUpdateInfo,
                                           AppUpdateCriteriaChecker appUpdateCriteriaChecker,
                                           Callback callback) {
        PlayStoreAppUpdateAvailability appUpdateAvailability = new PlayStoreAppUpdateAvailability(appUpdateInfo);
        Boolean meetsCriteria = null;
        try {
            meetsCriteria = appUpdateCriteriaChecker.meetsAppUpdateCriteria(appUpdateAvailability);
        } catch (Throwable error) {
            callback.onAppUpdateAvailableCheckFailed(error);
        }
        Optional.ofNullable(meetsCriteria).ifPresent(metCriteria -> {
            if (metCriteria) {
                callback.onAppUpdateAvailable(appUpdateAvailability);
            } else {
                callback.onAppUpdateCriteriaNotMet();
            }
        });
    }
}
