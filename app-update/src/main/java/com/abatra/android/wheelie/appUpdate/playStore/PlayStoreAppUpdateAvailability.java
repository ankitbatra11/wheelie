package com.abatra.android.wheelie.appUpdate.playStore;

import androidx.annotation.NonNull;

import com.abatra.android.wheelie.appUpdate.AppUpdateAvailability;
import com.abatra.android.wheelie.appUpdate.AppUpdateType;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.install.model.UpdateAvailability;

import java.util.Optional;

public class PlayStoreAppUpdateAvailability implements AppUpdateAvailability {

    final AppUpdateInfo appUpdateInfo;

    public PlayStoreAppUpdateAvailability(AppUpdateInfo appUpdateInfo) {
        this.appUpdateInfo = appUpdateInfo;
    }

    @Override
    public boolean isUpdateAvailable() {
        return appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE;
    }

    @Override
    public boolean isAppUpdateAllowedOfType(AppUpdateType appUpdateType) {
        return appUpdateInfo.isUpdateTypeAllowed(appUpdateType.getPlayStoreAppUpdateType());
    }

    @Override
    public boolean isClientVersionOlderThanNDays(int days) {
        return Optional.ofNullable(appUpdateInfo.clientVersionStalenessDays()).orElse(0) > days;
    }

    public AppUpdateInfo getAppUpdateInfo() {
        return appUpdateInfo;
    }

    @NonNull
    @Override
    public String toString() {
        return "PlayStoreAppUpdateAvailability{" +
                "appUpdateInfo=" + appUpdateInfo +
                '}';
    }
}
