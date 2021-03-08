package com.abatra.android.wheelie.update;

import android.content.Intent;

import androidx.annotation.Nullable;

import com.abatra.android.wheelie.lifecycle.LifecycleObserverObservable;
import com.google.android.play.core.appupdate.AppUpdateInfo;

public interface AppUpdateRequestor extends LifecycleObserverObservable<AppUpdateRequestor.Observer> {

    void requestAppUpdate(AppUpdateRequest appUpdateRequest);

    AppUpdateRequestResult onAppUpdateRequestResult(int resultCode, @Nullable Intent intent);

    void registerInstallStatusListener();

    void installDownloadedUpdate();

    interface Observer {

        void onRequestAppUpdateFailure(Throwable error);

        void onAppUpdateDownloadProgressChange(long bytesDownloaded, long totalBytesToDownload);

        void onAppUpdateDownloaded();

        void onAppUpdateDownloadFailure();

        void onInstallingAppUpdate();

        void onAppUpdateInstalled();

        void onAppUpdateInstallFailure();

        void onImmediateAppUpdateInProgress(AppUpdateInfo result);

        void onAppUpdateUnknownFailure();
    }
}
