package com.abatra.android.wheelie.update.playstore;

import android.content.Context;

import com.abatra.android.wheelie.update.AppUpdateAvailabilityChecker;
import com.abatra.android.wheelie.update.AppUpdateHandler;
import com.abatra.android.wheelie.update.AppUpdateRequestor;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;

import dagger.Module;
import dagger.Provides;

@Module
public class PlayStoreAppUpdateModule {

    @Provides
    protected AppUpdateManager appUpdateManager(Context context) {
        return AppUpdateManagerFactory.create(context);
    }

    @Provides
    protected AppUpdateAvailabilityChecker appUpdateAvailabilityChecker(AppUpdateManager appUpdateManager) {
        return new PlayStoreAppUpdateAvailabilityChecker(appUpdateManager);
    }

    @Provides
    protected AppUpdateRequestor appUpdateRequestor(AppUpdateManager appUpdateManager) {
        return new PlayStoreAppUpdateRequestor(appUpdateManager);
    }

    @Provides
    protected AppUpdateHandler appUpdateHandler(AppUpdateAvailabilityChecker appUpdateAvailabilityChecker,
                                                AppUpdateRequestor appUpdateRequestor) {
        return new PlayStoreAppUpdateHandler(appUpdateAvailabilityChecker, appUpdateRequestor);
    }

}
