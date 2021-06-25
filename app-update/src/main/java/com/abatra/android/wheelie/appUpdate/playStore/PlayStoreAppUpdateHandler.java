package com.abatra.android.wheelie.appUpdate.playStore;

import android.content.Intent;

import androidx.annotation.Nullable;

import com.abatra.android.wheelie.appUpdate.AppUpdateAvailabilityChecker;
import com.abatra.android.wheelie.appUpdate.AppUpdateAvailabilityCriteria;
import com.abatra.android.wheelie.appUpdate.AppUpdateHandler;
import com.abatra.android.wheelie.appUpdate.AppUpdateRequest;
import com.abatra.android.wheelie.appUpdate.AppUpdateRequestResult;
import com.abatra.android.wheelie.appUpdate.AppUpdateRequestor;
import com.abatra.android.wheelie.lifecycle.owner.ILifecycleOwner;

public class PlayStoreAppUpdateHandler implements AppUpdateHandler {

    private final AppUpdateAvailabilityChecker appUpdateAvailabilityChecker;
    private final AppUpdateRequestor appUpdateRequestor;

    public PlayStoreAppUpdateHandler(AppUpdateAvailabilityChecker appUpdateAvailabilityChecker,
                                     AppUpdateRequestor appUpdateRequestor) {
        this.appUpdateAvailabilityChecker = appUpdateAvailabilityChecker;
        this.appUpdateRequestor = appUpdateRequestor;
    }

    @Override
    public void observeLifecycle(ILifecycleOwner lifecycleOwner) {
        appUpdateRequestor.observeLifecycle(lifecycleOwner);
    }

    @Override
    public void addObserver(Observer observer) {
        appUpdateRequestor.addObserver(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        appUpdateRequestor.removeObserver(observer);
    }

    @Override
    public void removeObservers() {
        appUpdateRequestor.removeObservers();
    }

    @Override
    public void checkAppUpdateAvailability(AppUpdateAvailabilityCriteria availabilityCriteria, Callback callback) {
        appUpdateAvailabilityChecker.checkAppUpdateAvailability(availabilityCriteria, callback);
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
    public void installDownloadedUpdate() {
        appUpdateRequestor.installDownloadedUpdate();
    }
}
