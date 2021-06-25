package com.abatra.android.wheelie.appUpdate;

import android.content.Intent;

import androidx.annotation.Nullable;

import com.abatra.android.wheelie.lifecycle.observer.LifecycleObserverObservable;

public interface AppUpdateRequestor extends LifecycleObserverObservable<AppUpdateRequestor.Observer> {

    void requestAppUpdate(AppUpdateRequest appUpdateRequest);

    AppUpdateRequestResult onAppUpdateRequestResult(int resultCode, @Nullable Intent intent);

    void installDownloadedUpdate();

    interface Observer {

        void onRequestAppUpdateFailure(Throwable error);

        void onAppUpdatePendingDownload();

        void onAppUpdateDownloadProgressChange(long bytesDownloaded, long totalBytesToDownload);

        void onAppUpdateDownloaded();

        void onAppUpdateDownloadFailure();

        void onInstallingAppUpdate();

        void onAppUpdateInstalled();

        void onAppUpdateInstallFailure();

        void onImmediateAppUpdateInProgress(AppUpdateAvailability appUpdateAvailability);

        void onAppUpdateUnknownFailure();
    }
}
