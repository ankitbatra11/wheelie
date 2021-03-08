package com.abatra.android.wheelie.update.playstore;

import com.abatra.android.wheelie.thread.SaferTask;
import com.abatra.android.wheelie.update.AppUpdateAvailabilityChecker;
import com.abatra.android.wheelie.update.AppUpdateAvailabilityCriteria;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;

import java.util.concurrent.Executor;

import bolts.Task;
import timber.log.Timber;

import static com.abatra.android.wheelie.thread.BoltsUtils.getResult;

public class PlayStoreAppUpdateAvailabilityChecker implements AppUpdateAvailabilityChecker {

    private final AppUpdateManager appUpdateManager;
    Executor appUpdateCriteriaCheckExecutor = Task.BACKGROUND_EXECUTOR;

    public PlayStoreAppUpdateAvailabilityChecker(AppUpdateManager appUpdateManager) {
        this.appUpdateManager = appUpdateManager;
    }

    @Override
    public void checkAppUpdateAvailability(AppUpdateAvailabilityCriteria appUpdateAvailabilityCriteria, Callback callback) {
        appUpdateManager.getAppUpdateInfo().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                onGetAppUpdateInfoSuccess(task.getResult(), appUpdateAvailabilityCriteria, callback);
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
