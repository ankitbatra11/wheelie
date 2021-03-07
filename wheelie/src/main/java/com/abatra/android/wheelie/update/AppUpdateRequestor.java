package com.abatra.android.wheelie.update;

import android.content.Intent;

import androidx.annotation.Nullable;

public interface AppUpdateRequestor {

    void requestAppUpdate(AppUpdateRequest appUpdateRequest);

    AppUpdateRequestResult onAppUpdateRequestResult(int resultCode, @Nullable Intent intent);

    void registerInstallStatusListener();

    interface Observer {

        void onAppUpdateDownloadProgressChanged(long bytesDownloaded, long totalBytesToDownload);

        void onAppUpdateDownloaded();

        void onAppUpdateInstallFailed();
    }
}
