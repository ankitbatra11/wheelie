package com.abatra.android.wheelie.update.playstore.fake;

import android.content.Context;

import com.abatra.android.wheelie.update.AppUpdateAvailabilityChecker;
import com.abatra.android.wheelie.update.AppUpdateHandler;
import com.abatra.android.wheelie.update.AppUpdateRequestor;
import com.abatra.android.wheelie.update.AppUpdateType;
import com.abatra.android.wheelie.update.playstore.PlayStoreAppUpdateAvailabilityChecker;
import com.abatra.android.wheelie.update.playstore.PlayStoreAppUpdateHandler;
import com.google.android.play.core.appupdate.testing.FakeAppUpdateManager;

public class FakePlayStoreAppUpdateHandlerFactory {

    private final Context context;

    public FakePlayStoreAppUpdateHandlerFactory(Context context) {
        this.context = context;
    }

    public AppUpdateHandler simulateFlexibleUpdateAvailableToDownloadButInstall() {
        FakeAppUpdateManager fakeAppUpdateManager = makeAppUpdateAvailable(AppUpdateType.FLEXIBLE);
        return createAppUpdateHandler(fakeAppUpdateManager, new InstallFailsFlexibleFakePlayStoreAppUpdateRequestor(fakeAppUpdateManager));
    }

    public AppUpdateHandler simulateFlexibleUpdateAvailableToDownloadButDownloadFails() {
        FakeAppUpdateManager fakeAppUpdateManager = makeAppUpdateAvailable(AppUpdateType.FLEXIBLE);
        return createAppUpdateHandler(fakeAppUpdateManager, new DownloadFailsFlexibleFakePlayStoreAppUpdateRequestor(fakeAppUpdateManager));
    }

    public AppUpdateHandler simulateFlexibleUpdateAvailableToDownloadAndInstall() {
        FakeAppUpdateManager fakeAppUpdateManager = makeAppUpdateAvailable(AppUpdateType.FLEXIBLE);
        return createAppUpdateHandler(fakeAppUpdateManager, new FlexibleFakePlayStoreAppUpdateRequestor(fakeAppUpdateManager));
    }

    private FakeAppUpdateManager makeAppUpdateAvailable(AppUpdateType appUpdateType) {
        FakeAppUpdateManager fakeAppUpdateManager = new FakeAppUpdateManager(context);
        fakeAppUpdateManager.setUpdateAvailable(Integer.MAX_VALUE, appUpdateType.getPlayStoreAppUpdateType());
        return fakeAppUpdateManager;
    }

    private AppUpdateHandler createAppUpdateHandler(FakeAppUpdateManager fakeAppUpdateManager,
                                                    AppUpdateRequestor appUpdateRequestor) {
        AppUpdateAvailabilityChecker appUpdateAvailabilityChecker = new PlayStoreAppUpdateAvailabilityChecker(fakeAppUpdateManager);
        return new PlayStoreAppUpdateHandler(appUpdateAvailabilityChecker, appUpdateRequestor);
    }

    public AppUpdateHandler simulateImmediateUpdateAvailableToInstall() {
        FakeAppUpdateManager fakeAppUpdateManager = makeAppUpdateAvailable(AppUpdateType.IMMEDIATE);
        return createAppUpdateHandler(fakeAppUpdateManager, new ImmediateFakePlayStoreAppUpdateRequestor(fakeAppUpdateManager));
    }
}
