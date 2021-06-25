package com.abatra.android.wheelie.appUpdate.playStore.fake;

import androidx.annotation.NonNull;

import com.google.android.play.core.appupdate.testing.FakeAppUpdateManager;
import com.google.android.play.core.install.InstallState;
import com.google.android.play.core.install.model.InstallStatus;

public class DownloadFailsFlexibleFakePlayStoreAppUpdateRequestor extends FlexibleFakePlayStoreAppUpdateRequestor {

    public DownloadFailsFlexibleFakePlayStoreAppUpdateRequestor(FakeAppUpdateManager fakeAppUpdateManager) {
        super(fakeAppUpdateManager);
    }

    @Override
    public void onStateUpdate(@NonNull InstallState state) {
        super.onStateUpdate(state);
        if (state.installStatus() == InstallStatus.DOWNLOADING) {
            fakeDownloadFailure();
        }
    }
}
