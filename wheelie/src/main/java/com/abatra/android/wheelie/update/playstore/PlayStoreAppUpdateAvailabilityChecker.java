package com.abatra.android.wheelie.update.playstore;

import com.abatra.android.wheelie.update.AppUpdateAvailabilityChecker;
import com.abatra.android.wheelie.update.AppUpdateCriteria;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;

import timber.log.Timber;

import static com.abatra.android.wheelie.thread.BoltsUtils.getResult;
import static com.abatra.android.wheelie.thread.SaferTask.backgroundTask;

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
        Timber.d("onGetAppUpdateInfoSuccess appUpdateInfo=%s", appUpdateInfo);
        final PlayStoreAppUpdateAvailability appUpdateAvailability = new PlayStoreAppUpdateAvailability(appUpdateInfo);
        backgroundTask(() ->
        {
            AppUpdateCriteria criteria = AppUpdateCriteria.IS_UPDATE_AVAILABLE.and(appUpdateCriteria);
            return criteria.meets(new PlayStoreAppUpdateAvailability(appUpdateInfo));

        }).continueOnUiThread(task ->
        {
            if (task.getError() != null) {
                callback.onAppUpdateAvailableCheckFailed(task.getError());
            } else {
                getResult(task).ifPresent(metCriteria -> {
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
