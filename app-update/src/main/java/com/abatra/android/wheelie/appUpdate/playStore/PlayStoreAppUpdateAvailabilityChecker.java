package com.abatra.android.wheelie.appUpdate.playStore;

import com.abatra.android.wheelie.appUpdate.AppUpdateAvailabilityChecker;
import com.abatra.android.wheelie.appUpdate.AppUpdateAvailabilityCriteria;
import com.abatra.android.wheelie.core.async.bolts.BoltsUtils;
import com.abatra.android.wheelie.core.async.bolts.SaferTask;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;

import java.util.concurrent.Executor;

import bolts.Task;
import timber.log.Timber;

public class PlayStoreAppUpdateAvailabilityChecker implements AppUpdateAvailabilityChecker {

    private final AppUpdateManager appUpdateManager;
    Executor appUpdateCriteriaCheckExecutor = Task.BACKGROUND_EXECUTOR;

    public PlayStoreAppUpdateAvailabilityChecker(AppUpdateManager appUpdateManager) {
        this.appUpdateManager = appUpdateManager;
    }

    @Override
    public void checkAppUpdateAvailability(AppUpdateAvailabilityCriteria availabilityCriteria, Callback callback) {
        appUpdateManager.getAppUpdateInfo().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                onGetAppUpdateInfoSuccess(task.getResult(), availabilityCriteria, callback);
            } else {
                Timber.e(task.getException(), "App update availability check failed!");
                callback.onAppUpdateAvailableCheckFailed(task.getException());
            }
        });
    }

    private void onGetAppUpdateInfoSuccess(AppUpdateInfo appUpdateInfo,
                                           AppUpdateAvailabilityCriteria appUpdateAvailabilityCriteria,
                                           Callback callback) {
        Timber.d("onGetAppUpdateInfoSuccess appUpdateInfo=%s", appUpdateInfo);
        final PlayStoreAppUpdateAvailability appUpdateAvailability = new PlayStoreAppUpdateAvailability(appUpdateInfo);
        SaferTask.callOn(appUpdateCriteriaCheckExecutor, () ->
        {
            AppUpdateAvailabilityCriteria criteria = AppUpdateAvailabilityCriteria.IS_UPDATE_AVAILABLE.and(appUpdateAvailabilityCriteria);
            return criteria.meets(new PlayStoreAppUpdateAvailability(appUpdateInfo));

        }).continueOnUiThread(task ->
        {
            if (task.getError() != null) {
                callback.onAppUpdateAvailableCheckFailed(task.getError());
            } else {
                BoltsUtils.getResult(task).ifPresent(metCriteria -> {
                    if (metCriteria) {
                        callback.onAppUpdateAvailable(appUpdateAvailability);
                    } else {
                        callback.onAppUpdateCriteriaNotMet();
                    }
                });
            }
            return null;
        });
    }
}
