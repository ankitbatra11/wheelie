package com.abatra.android.wheelie.update.playstore;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.abatra.android.wheelie.pattern.Observable;
import com.abatra.android.wheelie.update.AppUpdateRequest;
import com.abatra.android.wheelie.update.AppUpdateRequestResult;
import com.abatra.android.wheelie.update.AppUpdateRequestor;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.install.InstallState;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.ActivityResult;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.tasks.Task;

import timber.log.Timber;

public class PlayStoreAppUpdateRequestor implements AppUpdateRequestor, InstallStateUpdatedListener {

    private final AppUpdateManager appUpdateManager;
    private final Observable<Observer> observers = Observable.copyOnWriteArraySet();

    public PlayStoreAppUpdateRequestor(AppUpdateManager appUpdateManager) {
        this.appUpdateManager = appUpdateManager;
    }

    @Override
    public void addObserver(Observer observer) {
        observers.addObserver(observer);
    }

    @Override
    public void requestAppUpdate(AppUpdateRequest appUpdateRequest) {
        try {
            PlayStoreAppUpdateRequest storeAppUpdateRequest = (PlayStoreAppUpdateRequest) appUpdateRequest;
            appUpdateManager.startUpdateFlowForResult(
                    storeAppUpdateRequest.getAppUpdateAvailability().getAppUpdateInfo(),
                    storeAppUpdateRequest.getRequestedAppUpdateType().getPlayStoreAppUpdateType(),
                    storeAppUpdateRequest.getActivity(),
                    storeAppUpdateRequest.getReqCode());
        } catch (Throwable error) {
            Timber.e(error);
            observers.forEachObserver(Observer::onAppUpdateInstallFailed);
        }
    }

    @Override
    public AppUpdateRequestResult onAppUpdateRequestResult(int resultCode, @Nullable Intent intent) {
        AppUpdateRequestResult result;
        switch (resultCode) {
            case ActivityResult.RESULT_IN_APP_UPDATE_FAILED:
                result = AppUpdateRequestResult.IN_APP_UPDATE_FAILED;
                break;
            case Activity.RESULT_OK:
                result = AppUpdateRequestResult.ACCEPTED;
                break;
            case Activity.RESULT_CANCELED:
                result = AppUpdateRequestResult.CANCELED_OR_DENIED;
                break;
            default:
                throw new IllegalArgumentException("Unknown app update resultCode=" + resultCode);
        }
        Timber.d("resultCode=%d result=%s", resultCode, result);
        return result;
    }

    @Override
    @SuppressLint("SwitchIntDef")
    public void onStateUpdate(@NonNull InstallState state) {
        Timber.d("installStatus=%d", state.installStatus());
        switch (state.installStatus()) {
            case InstallStatus.DOWNLOADED:
                observers.forEachObserver(Observer::onAppUpdateDownloaded);
                break;
            case InstallStatus.DOWNLOADING:
                observers.forEachObserver(type -> {
                    long bytesDownloaded = state.bytesDownloaded();
                    long totalBytesToDownload = state.totalBytesToDownload();
                    type.onAppUpdateDownloadProgressChanged(bytesDownloaded, totalBytesToDownload);
                });
                break;
            case InstallStatus.FAILED:
                observers.forEachObserver(Observer::onAppUpdateInstallFailed);
                break;
        }
    }

    @Override
    public void registerInstallStatusListener() {
        appUpdateManager.registerListener(this);
    }

    @Override
    public void installDownloadedUpdate() {
        appUpdateManager.completeUpdate().addOnFailureListener(e -> Timber.e(e, "appUpdateManager.completeUpdate failed!"));
    }

    @Override
    public void onResume() {
        Task<AppUpdateInfo> appUpdateInfo = appUpdateManager.getAppUpdateInfo();
        appUpdateInfo.addOnFailureListener(e -> Timber.e(e, "onResume: appUpdateManager.getAppUpdateInfo failed!"))
                .addOnSuccessListener(result -> {
                    if (result.installStatus() == InstallStatus.DOWNLOADED) {
                        observers.forEachObserver(Observer::onAppUpdateDownloaded);
                    }
                });
    }

    @Override
    public void onDestroy() {
        appUpdateManager.unregisterListener(this);
        observers.removeObservers();
    }
}
