package com.abatra.android.wheelie.update.playstore.fake;

import android.os.SystemClock;

import androidx.annotation.Nullable;

import com.abatra.android.wheelie.thread.AndroidThread;
import com.abatra.android.wheelie.thread.HandlerAndroidThread;
import com.abatra.android.wheelie.update.playstore.PlayStoreAppUpdateRequest;
import com.google.android.play.core.appupdate.testing.FakeAppUpdateManager;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static com.abatra.android.wheelie.thread.SaferTask.backgroundTask;
import static com.abatra.android.wheelie.thread.SaferTask.uiTask;
import static com.google.common.base.Preconditions.checkState;

public class DownloadFakePlayStoreAppUpdateRequestor extends FakePlayStoreAppUpdateRequestor {

    public static final String THREAD_NAME = "simulateFakeAppUpdateDownloadProgress";
    @Nullable
    private DownloadProgressSimulator downloadProgressSimulator;

    public DownloadFakePlayStoreAppUpdateRequestor(FakeAppUpdateManager fakeAppUpdateManager) {
        super(fakeAppUpdateManager);
    }

    @Override
    protected void startAppUpdateFlowOrThrow(PlayStoreAppUpdateRequest storeAppUpdateRequest) {
        super.startAppUpdateFlowOrThrow(storeAppUpdateRequest);
        getDownloadProgressSimulator().ifPresent(DownloadProgressSimulator::stop);
        downloadProgressSimulator = new DownloadProgressSimulator(new HandlerAndroidThread(THREAD_NAME));
        downloadProgressSimulator.start();
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
            getFakeAppUpdateManager().installCompletes();
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

            checkState(getFakeAppUpdateManager().isConfirmationDialogVisible());
            getFakeAppUpdateManager().userAcceptsUpdate();
            getFakeAppUpdateManager().downloadStarts();

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
                    stop();
                }
                return null;
            });
        }

        private void stop() {
            androidThread.stopAndroidThread();
        }
    }
}
