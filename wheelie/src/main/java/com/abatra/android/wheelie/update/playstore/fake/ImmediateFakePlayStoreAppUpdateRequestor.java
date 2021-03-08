package com.abatra.android.wheelie.update.playstore.fake;

import com.abatra.android.wheelie.resource.Text;
import com.abatra.android.wheelie.update.AppUpdateRequest;
import com.abatra.android.wheelie.widget.Toaster;
import com.google.android.play.core.appupdate.testing.FakeAppUpdateManager;
import com.google.common.base.Preconditions;

public class ImmediateFakePlayStoreAppUpdateRequestor extends FakePlayStoreAppUpdateRequestor {

    protected ImmediateFakePlayStoreAppUpdateRequestor(FakeAppUpdateManager fakeAppUpdateManager) {
        super(fakeAppUpdateManager);
    }

    @Override
    public void requestAppUpdate(AppUpdateRequest appUpdateRequest) {
        super.requestAppUpdate(appUpdateRequest);

        Preconditions.checkState(getFakeAppUpdateManager().isImmediateFlowVisible());
        Toaster.toastShort(appUpdateRequest.getActivity(), Text.string("immediateFlowVisible=true"));

        getFakeAppUpdateManager().userAcceptsUpdate();
        getFakeAppUpdateManager().downloadStarts();
        getFakeAppUpdateManager().downloadCompletes();
        getFakeAppUpdateManager().completeUpdate();
        getFakeAppUpdateManager().installCompletes();

        Preconditions.checkState(!getFakeAppUpdateManager().isImmediateFlowVisible());
        Toaster.toastShort(appUpdateRequest.getActivity(), Text.string("installCompleted isImmediateFlowVisible=false"));
    }
}
