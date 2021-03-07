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
abstract public class PlayStoreAppUpdateModule {

    @Provides
    static AppUpdateManager appUpdateManager(Context context) {
        return AppUpdateManagerFactory.create(context);
    }

    @Provides
    static AppUpdateAvailabilityChecker appUpdateAvailabilityChecker(AppUpdateManager appUpdateManager) {
        return new PlayStoreAppUpdateAvailabilityChecker(appUpdateManager);
    }

    @Provides
    static AppUpdateRequestor appUpdateRequestor(AppUpdateManager appUpdateManager) {
        return new PlayStoreAppUpdateRequestor(appUpdateManager);
    }

    @Provides
    static AppUpdateHandler appUpdateHandler(AppUpdateAvailabilityChecker appUpdateAvailabilityChecker,
                                             AppUpdateRequestor appUpdateRequestor) {
        return new PlayStoreAppUpdateHandler(appUpdateAvailabilityChecker, appUpdateRequestor);
    }

}
