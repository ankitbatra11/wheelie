package com.abatra.android.wheelie.update.playstore;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.abatra.android.wheelie.update.AppUpdateRequest;
import com.abatra.android.wheelie.update.AppUpdateRequestResult;
import com.abatra.android.wheelie.update.AppUpdateRequestor;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.install.InstallState;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.ActivityResult;
import com.google.android.play.core.install.model.InstallStatus;

import java.util.Optional;

import timber.log.Timber;

public class PlayStoreAppUpdateRequestor implements AppUpdateRequestor, InstallStateUpdatedListener {

    private final AppUpdateManager appUpdateManager;
    @Nullable
    private PlayStoreAppUpdateRequest updateAppRequest;

    public PlayStoreAppUpdateRequestor(AppUpdateManager appUpdateManager) {
        this.appUpdateManager = appUpdateManager;
    }

    @Override
    public void requestAppUpdate(AppUpdateRequest appUpdateRequest) {
        this.updateAppRequest = (PlayStoreAppUpdateRequest) appUpdateRequest;
        this.updateAppRequest.getRequestedAppUpdateType().beforeStartingAppUpdateFlow(this);
        try {

            appUpdateManager.startUpdateFlowForResult(
                    this.updateAppRequest.getAppUpdateAvailability().getAppUpdateInfo(),
                    this.updateAppRequest.getRequestedAppUpdateType().getPlayStoreAppUpdateType(),
                    this.updateAppRequest.getActivity(),
                    this.updateAppRequest.getReqCode());

        } catch (IntentSender.SendIntentException e) {
            throw new RuntimeException(e);
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

        Optional.ofNullable(updateAppRequest)
                .flatMap(AppUpdateRequest::getObserver)
                .ifPresent(observer -> {
                    switch (state.installStatus()) {
                        case InstallStatus.DOWNLOADED:
                            observer.onAppUpdateDownloaded();
                            break;
                        case InstallStatus.DOWNLOADING:
                            observer.onAppUpdateDownloadProgressChanged(state.bytesDownloaded(), state.totalBytesToDownload());
                            break;
                        case InstallStatus.FAILED:
                            observer.onAppUpdateInstallFailed();
                            break;
                    }
                });
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
    public void onDestroy() {
        appUpdateManager.unregisterListener(this);
        updateAppRequest = null;
    }
}
