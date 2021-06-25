package com.abatra.android.wheelie.appUpdate.playStore.fake;

import com.abatra.android.wheelie.appUpdate.AppUpdateRequest;
import com.google.android.play.core.appupdate.testing.FakeAppUpdateManager;

import timber.log.Timber;

import static com.google.common.base.Preconditions.checkState;

public class ImmediateFakePlayStoreAppUpdateRequestor extends FakePlayStoreAppUpdateRequestor {

    public ImmediateFakePlayStoreAppUpdateRequestor(FakeAppUpdateManager fakeAppUpdateManager) {
        super(fakeAppUpdateManager);
    }

    @Override
    public void requestAppUpdate(AppUpdateRequest appUpdateRequest) {
        super.requestAppUpdate(appUpdateRequest);

        checkState(getFakeAppUpdateManager().isImmediateFlowVisible());
        Timber.d("immediateFlowVisible=true");

        getFakeAppUpdateManager().userAcceptsUpdate();
        getFakeAppUpdateManager().downloadStarts();
        getFakeAppUpdateManager().downloadCompletes();
        getFakeAppUpdateManager().completeUpdate();
        getFakeAppUpdateManager().installCompletes();

        checkState(!getFakeAppUpdateManager().isImmediateFlowVisible());
        Timber.d("installCompleted isImmediateFlowVisible=false");
    }
}
