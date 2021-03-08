package com.abatra.android.wheelie.update.playstore.fake;

import com.abatra.android.wheelie.update.playstore.PlayStoreAppUpdateRequestor;
import com.google.android.play.core.appupdate.testing.FakeAppUpdateManager;

public class FakePlayStoreAppUpdateRequestor extends PlayStoreAppUpdateRequestor {

    protected FakePlayStoreAppUpdateRequestor(FakeAppUpdateManager fakeAppUpdateManager) {
        super(fakeAppUpdateManager);
    }

    protected FakeAppUpdateManager getFakeAppUpdateManager() {
        return (FakeAppUpdateManager) getAppUpdateManager();
    }
}
