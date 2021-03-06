package com.abatra.android.wheelie.appUpdate;

import android.content.Context;

import com.abatra.android.wheelie.appUpdate.playStore.PlayStoreAppUpdateAvailabilityChecker;
import com.abatra.android.wheelie.appUpdate.playStore.PlayStoreAppUpdateHandler;
import com.abatra.android.wheelie.appUpdate.playStore.PlayStoreAppUpdateRequestor;
import com.abatra.android.wheelie.appUpdate.playStore.fake.DownloadFailsFlexibleFakePlayStoreAppUpdateRequestor;
import com.abatra.android.wheelie.appUpdate.playStore.fake.FlexibleFakePlayStoreAppUpdateRequestor;
import com.abatra.android.wheelie.appUpdate.playStore.fake.ImmediateFakePlayStoreAppUpdateRequestor;
import com.abatra.android.wheelie.appUpdate.playStore.fake.InstallFailsFlexibleFakePlayStoreAppUpdateRequestor;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.appupdate.testing.FakeAppUpdateManager;

public class AppUpdateHandlerFactory {

    private final Context context;

    public AppUpdateHandlerFactory(Context context) {
        this.context = context;
    }

    public AppUpdateHandler playStoreAppUpdateHandler() {
        AppUpdateManager appUpdateManager = AppUpdateManagerFactory.create(context);
        return createPlayStoreAppUpdateHandler(appUpdateManager, new PlayStoreAppUpdateRequestor(appUpdateManager));
    }

    public AppUpdateHandler fakePlayStoreFlexibleUpdateAvailableToDownloadButInstall() {
        FakeAppUpdateManager fakeAppUpdateManager = makeAppUpdateAvailable(AppUpdateType.FLEXIBLE);
        return createPlayStoreAppUpdateHandler(fakeAppUpdateManager,
                new InstallFailsFlexibleFakePlayStoreAppUpdateRequestor(fakeAppUpdateManager));
    }

    public AppUpdateHandler fakePlayStoreFlexibleUpdateAvailableToDownloadButDownloadFails() {
        FakeAppUpdateManager fakeAppUpdateManager = makeAppUpdateAvailable(AppUpdateType.FLEXIBLE);
        return createPlayStoreAppUpdateHandler(fakeAppUpdateManager,
                new DownloadFailsFlexibleFakePlayStoreAppUpdateRequestor(fakeAppUpdateManager));
    }

    public AppUpdateHandler fakePlayStoreFlexibleUpdateAvailableToDownloadAndInstall() {
        FakeAppUpdateManager fakeAppUpdateManager = makeAppUpdateAvailable(AppUpdateType.FLEXIBLE);
        return createPlayStoreAppUpdateHandler(fakeAppUpdateManager, new FlexibleFakePlayStoreAppUpdateRequestor(fakeAppUpdateManager));
    }

    private FakeAppUpdateManager makeAppUpdateAvailable(AppUpdateType appUpdateType) {
        FakeAppUpdateManager fakeAppUpdateManager = new FakeAppUpdateManager(context);
        fakeAppUpdateManager.setUpdateAvailable(Integer.MAX_VALUE, appUpdateType.getPlayStoreAppUpdateType());
        return fakeAppUpdateManager;
    }

    private AppUpdateHandler createPlayStoreAppUpdateHandler(AppUpdateManager appUpdateManager,
                                                             AppUpdateRequestor appUpdateRequestor) {
        AppUpdateAvailabilityChecker availabilityChecker = new PlayStoreAppUpdateAvailabilityChecker(appUpdateManager);
        return new PlayStoreAppUpdateHandler(availabilityChecker, appUpdateRequestor);
    }

    public AppUpdateHandler fakePlayStoreImmediateUpdateAvailableToInstall() {
        FakeAppUpdateManager fakeAppUpdateManager = makeAppUpdateAvailable(AppUpdateType.IMMEDIATE);
        return createPlayStoreAppUpdateHandler(fakeAppUpdateManager, new ImmediateFakePlayStoreAppUpdateRequestor(fakeAppUpdateManager));
    }
}
