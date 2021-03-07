package com.abatra.android.wheelie.update.playstore;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.abatra.android.wheelie.update.AppUpdateType;
import com.abatra.android.wheelie.update.AppUpdateRequest;

public class PlayStoreAppUpdateRequest extends AppUpdateRequest {

    private final PlayStoreAppUpdateAvailability appUpdateAvailability;

    public PlayStoreAppUpdateRequest(AppUpdateType requestedAppUpdateType,
                                     Activity activity,
                                     int reqCode,
                                     PlayStoreAppUpdateAvailability appUpdateAvailability) {
        super(requestedAppUpdateType, activity, reqCode);
        this.appUpdateAvailability = appUpdateAvailability;
    }

    public PlayStoreAppUpdateAvailability getAppUpdateAvailability() {
        return appUpdateAvailability;
    }

    @NonNull
    @Override
    public String toString() {
        return "PlayStoreAppUpdateRequest{" +
                "super=" + super.toString() +
                ", appUpdateAvailability=" + appUpdateAvailability +
                '}';
    }
}
