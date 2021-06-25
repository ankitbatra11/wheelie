package com.abatra.android.wheelie.appUpdate.playStore.fake;

import com.google.android.play.core.appupdate.testing.FakeAppUpdateManager;

public class InstallFailsFlexibleFakePlayStoreAppUpdateRequestor extends FlexibleFakePlayStoreAppUpdateRequestor {

    public InstallFailsFlexibleFakePlayStoreAppUpdateRequestor(FakeAppUpdateManager fakeAppUpdateManager) {
        super(fakeAppUpdateManager);
    }

    @Override
    protected void fakeInstallCompletion() {
        fakeInstallFailure();
    }
}
