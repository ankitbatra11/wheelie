package com.abatra.android.wheelie.update.playstore.fake;

import android.content.Context;

import com.abatra.android.wheelie.update.AppUpdateAvailabilityChecker;
import com.abatra.android.wheelie.update.AppUpdateHandler;
import com.abatra.android.wheelie.update.AppUpdateRequestor;
import com.abatra.android.wheelie.update.playstore.PlayStoreAppUpdateAvailabilityChecker;
import com.abatra.android.wheelie.update.playstore.PlayStoreAppUpdateHandler;
import com.google.android.play.core.appupdate.testing.FakeAppUpdateManager;
import com.google.android.play.core.install.model.AppUpdateType;

public class FakePlayStoreAppUpdateHandlerFactory {

    private final Context context;

    public FakePlayStoreAppUpdateHandlerFactory(Context context) {
        this.context = context;
    }

    public AppUpdateHandler simulateFlexibleUpdateAvailableToDownloadAndInstall() {
        FakeAppUpdateManager fakeAppUpdateManager = new FakeAppUpdateManager(context);
        fakeAppUpdateManager.setUpdateAvailable(Integer.MAX_VALUE, AppUpdateType.FLEXIBLE);
        return createAppUpdateHandler(fakeAppUpdateManager, new DownloadFakePlayStoreAppUpdateRequestor(fakeAppUpdateManager));
    }

    private AppUpdateHandler createAppUpdateHandler(FakeAppUpdateManager fakeAppUpdateManager,
                                                    AppUpdateRequestor appUpdateRequestor) {
        AppUpdateAvailabilityChecker appUpdateAvailabilityChecker = new PlayStoreAppUpdateAvailabilityChecker(fakeAppUpdateManager);
        return new PlayStoreAppUpdateHandler(appUpdateAvailabilityChecker, appUpdateRequestor);
    }

    public AppUpdateHandler simulateImmediateUpdateAvailableToInstall() {
        FakeAppUpdateManager fakeAppUpdateManager = new FakeAppUpdateManager(context);
        fakeAppUpdateManager.setUpdateAvailable(Integer.MAX_VALUE, AppUpdateType.IMMEDIATE);
        return createAppUpdateHandler(fakeAppUpdateManager, new ImmediateFakePlayStoreAppUpdateRequestor(fakeAppUpdateManager));
    }
}
