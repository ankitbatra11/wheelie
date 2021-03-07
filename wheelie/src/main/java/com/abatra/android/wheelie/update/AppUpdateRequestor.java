package com.abatra.android.wheelie.update;

import android.content.Intent;

import androidx.annotation.Nullable;

import com.abatra.android.wheelie.lifecycle.LifecycleObserverObservable;

public interface AppUpdateRequestor extends LifecycleObserverObservable<AppUpdateRequestor.Observer> {

    void requestAppUpdate(AppUpdateRequest appUpdateRequest);

    AppUpdateRequestResult onAppUpdateRequestResult(int resultCode, @Nullable Intent intent);

    void registerInstallStatusListener();

    void installDownloadedUpdate();

    interface Observer {

        void onAppUpdateDownloadProgressChanged(long bytesDownloaded, long totalBytesToDownload);

        void onAppUpdateDownloaded();

        void onAppUpdateInstallFailed();
    }
}
