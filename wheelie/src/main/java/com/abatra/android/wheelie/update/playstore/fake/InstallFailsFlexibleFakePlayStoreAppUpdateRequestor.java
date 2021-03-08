package com.abatra.android.wheelie.update.playstore.fake;

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
