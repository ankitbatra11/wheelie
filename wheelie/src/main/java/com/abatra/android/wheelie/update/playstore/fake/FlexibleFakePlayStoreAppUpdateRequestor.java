package com.abatra.android.wheelie.update.playstore.fake;

import android.annotation.SuppressLint;
import android.os.SystemClock;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.abatra.android.wheelie.thread.AndroidThread;
import com.abatra.android.wheelie.thread.HandlerAndroidThread;
import com.abatra.android.wheelie.update.playstore.PlayStoreAppUpdateRequest;
import com.google.android.play.core.appupdate.testing.FakeAppUpdateManager;
import com.google.android.play.core.install.InstallState;
import com.google.android.play.core.install.model.InstallStatus;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static com.abatra.android.wheelie.thread.SaferTask.backgroundTask;
import static com.abatra.android.wheelie.thread.SaferTask.uiTask;
import static com.google.common.base.Preconditions.checkState;

public class FlexibleFakePlayStoreAppUpdateRequestor extends FakePlayStoreAppUpdateRequestor {

    public static final String THREAD_NAME = "simulateFakeAppUpdateDownloadProgress";
    @Nullable
    private DownloadProgressSimulator downloadProgressSimulator;

    public FlexibleFakePlayStoreAppUpdateRequestor(FakeAppUpdateManager fakeAppUpdateManager) {
        super(fakeAppUpdateManager);
    }

    @Override
    protected void startAppUpdateFlowOrThrow(PlayStoreAppUpdateRequest storeAppUpdateRequest) {
        super.startAppUpdateFlowOrThrow(storeAppUpdateRequest);
        checkState(getFakeAppUpdateManager().isConfirmationDialogVisible());
        fakeUserAcceptsUpdate();
        startFakeAppDownload();
    }

    @Override
    @SuppressLint("SwitchIntDef")
    public void onStateUpdate(@NonNull InstallState state) {
        switch (state.installStatus()) {
            case InstallStatus.DOWNLOADED:
            case InstallStatus.FAILED:
                getDownloadProgressSimulator().ifPresent(DownloadProgressSimulator::stop);
                break;
            case InstallStatus.DOWNLOADING:
                downloadProgressSimulator = getDownloadProgressSimulator().orElseGet(() -> {
                    downloadProgressSimulator = new DownloadProgressSimulator(new HandlerAndroidThread(THREAD_NAME));
                    downloadProgressSimulator.start();
                    return downloadProgressSimulator;
                });
                break;
        }
        super.onStateUpdate(state);

    }

    private Optional<DownloadProgressSimulator> getDownloadProgressSimulator() {
        return Optional.ofNullable(downloadProgressSimulator);
    }

    @Override
    public void installDownloadedUpdate() {
        super.installDownloadedUpdate();
        backgroundTask(() ->
        {
            SystemClock.sleep(TimeUnit.SECONDS.toMillis(2));
            return null;

        }).continueOnUiThread(task ->
        {
            fakeInstallCompletion();
            return null;
        });
    }

    @Override
    public void onDestroy() {
        getDownloadProgressSimulator().ifPresent(DownloadProgressSimulator::stop);
        super.onDestroy();
    }

    private class DownloadProgressSimulator implements Runnable {

        private int bytesDownloaded = 0;
        private static final int BYTES_TO_DOWNLOAD = 100;

        private final AndroidThread androidThread;

        private DownloadProgressSimulator(AndroidThread androidThread) {
            this.androidThread = androidThread;
        }

        private void start() {
            androidThread.stopAndroidThread();
            androidThread.startAndroidThread();
            androidThread.postRunnableWithScheduledInterval(this, 1, TimeUnit.SECONDS);
        }

        @Override
        public void run() {

            bytesDownloaded += 10;
            bytesDownloaded = Math.min(bytesDownloaded, BYTES_TO_DOWNLOAD);

            uiTask(() -> {
                getFakeAppUpdateManager().setTotalBytesToDownload(BYTES_TO_DOWNLOAD);
                getFakeAppUpdateManager().setBytesDownloaded(bytesDownloaded);
                if (bytesDownloaded == BYTES_TO_DOWNLOAD) {
                    getFakeAppUpdateManager().downloadCompletes();
                }
                return null;
            });
        }

        private void stop() {
            androidThread.stopAndroidThread();
        }
    }
}
