package com.abatra.android.wheelie.update.playstore;

import android.content.Intent;

import androidx.annotation.Nullable;

import com.abatra.android.wheelie.update.AppUpdateAvailabilityChecker;
import com.abatra.android.wheelie.update.AppUpdateCriteria;
import com.abatra.android.wheelie.update.AppUpdateHandler;
import com.abatra.android.wheelie.update.AppUpdateRequest;
import com.abatra.android.wheelie.update.AppUpdateRequestResult;
import com.abatra.android.wheelie.update.AppUpdateRequestor;

public class PlayStoreAppUpdateHandler implements AppUpdateHandler {

    private final AppUpdateAvailabilityChecker appUpdateAvailabilityChecker;
    private final AppUpdateRequestor appUpdateRequestor;

    public PlayStoreAppUpdateHandler(AppUpdateAvailabilityChecker appUpdateAvailabilityChecker,
                                     AppUpdateRequestor appUpdateRequestor) {
        this.appUpdateAvailabilityChecker = appUpdateAvailabilityChecker;
        this.appUpdateRequestor = appUpdateRequestor;
    }

    @Override
    public void checkAppUpdateAvailability(AppUpdateCriteria appUpdateCriteria, Callback callback) {
        appUpdateAvailabilityChecker.checkAppUpdateAvailability(appUpdateCriteria, callback);
    }

    @Override
    public void requestAppUpdate(AppUpdateRequest appUpdateRequest) {
        appUpdateRequestor.requestAppUpdate(appUpdateRequest);
    }

    @Override
    public AppUpdateRequestResult onAppUpdateRequestResult(int resultCode, @Nullable Intent intent) {
        return appUpdateRequestor.onAppUpdateRequestResult(resultCode, intent);
    }

    @Override
    public void registerInstallStatusListener() {
        appUpdateRequestor.registerInstallStatusListener();
    }

    @Override
    public void installDownloadedUpdate() {
        appUpdateRequestor.installDownloadedUpdate();
    }
}
