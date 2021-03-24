package com.abatra.android.wheelie.update.playstore.fake;

import com.abatra.android.wheelie.resource.text.Text;
import com.abatra.android.wheelie.update.AppUpdateRequest;
import com.abatra.android.wheelie.widget.Toaster;
import com.google.android.play.core.appupdate.testing.FakeAppUpdateManager;

import static com.google.common.base.Preconditions.checkState;

public class ImmediateFakePlayStoreAppUpdateRequestor extends FakePlayStoreAppUpdateRequestor {

    public ImmediateFakePlayStoreAppUpdateRequestor(FakeAppUpdateManager fakeAppUpdateManager) {
        super(fakeAppUpdateManager);
    }

    @Override
    public void requestAppUpdate(AppUpdateRequest appUpdateRequest) {
        super.requestAppUpdate(appUpdateRequest);

        checkState(getFakeAppUpdateManager().isImmediateFlowVisible());
        Toaster.toastShort(appUpdateRequest.getActivity(), Text.string("immediateFlowVisible=true"));

        getFakeAppUpdateManager().userAcceptsUpdate();
        getFakeAppUpdateManager().downloadStarts();
        getFakeAppUpdateManager().downloadCompletes();
        getFakeAppUpdateManager().completeUpdate();
        getFakeAppUpdateManager().installCompletes();

        checkState(!getFakeAppUpdateManager().isImmediateFlowVisible());
        Toaster.toastShort(appUpdateRequest.getActivity(), Text.string("installCompleted isImmediateFlowVisible=false"));
    }
}
