package com.abatra.android.wheelie.update.playstore;

import com.abatra.android.wheelie.update.AppUpdateAvailability;
import com.abatra.android.wheelie.update.AppUpdateAvailabilityChecker;
import com.abatra.android.wheelie.update.AppUpdateCriteria;
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
    public void checkAppUpdateAvailability(AppUpdateCriteria appUpdateCriteria, Callback callback) {
        appUpdateManager.getAppUpdateInfo().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                onGetAppUpdateInfoSuccess(task.getResult(), appUpdateCriteria, callback);
            } else {
                Timber.e(task.getException(), "App update availability check failed!");
                callback.onAppUpdateAvailableCheckFailed(task.getException());
            }
        });
    }

    private void onGetAppUpdateInfoSuccess(AppUpdateInfo appUpdateInfo,
                                           AppUpdateCriteria appUpdateCriteria,
                                           Callback callback) {
        PlayStoreAppUpdateAvailability appUpdateAvailability = new PlayStoreAppUpdateAvailability(appUpdateInfo);
        appUpdateCriteria = AppUpdateCriteria.IS_UPDATE_AVAILABLE.and(appUpdateCriteria);
        tryCheckingAppUpdateCriteria(appUpdateCriteria, appUpdateAvailability, callback).ifPresent(metCriteria -> {
            if (metCriteria) {
                callback.onAppUpdateAvailable(appUpdateAvailability);
            } else {
                callback.onAppUpdateCriteriaNotMet();
            }
        });
    }

    private Optional<Boolean> tryCheckingAppUpdateCriteria(AppUpdateCriteria appUpdateCriteria,
                                                           AppUpdateAvailability appUpdateAvailability,
                                                           Callback callback) {
        Boolean result = null;
        try {
            result = appUpdateCriteria.meets(appUpdateAvailability);
        } catch (Throwable error) {
            Timber.e(error);
            callback.onAppUpdateAvailableCheckFailed(error);
        }
        return Optional.ofNullable(result);
    }
}
