package com.abatra.android.wheelie.update.playstore;

import android.app.Activity;
import android.os.Build;

import com.abatra.android.wheelie.update.AppUpdateAvailabilityCriteria;
import com.abatra.android.wheelie.update.AppUpdateRequest;
import com.abatra.android.wheelie.update.AppUpdateRequestor;
import com.abatra.android.wheelie.update.AppUpdateType;
import com.google.android.play.core.appupdate.testing.FakeAppUpdateManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = Build.VERSION_CODES.P)
public class PlayStoreAppUpdateRequestorTest {

    private PlayStoreAppUpdateRequestor playStoreAppUpdateRequestor;
    private FakeAppUpdateManager fakeAppUpdateManager;

    @Mock
    private AppUpdateAvailabilityCriteria mockedAppUpdateAvailabilityCriteria;

    @Mock
    private AppUpdateRequestor.Observer mockedObserver;

    @Mock
    private Activity mockedActivity;

    @Before
    public void setup() {

        MockitoAnnotations.openMocks(this);

        when(mockedAppUpdateAvailabilityCriteria.meets(any())).thenReturn(true);

        fakeAppUpdateManager = new FakeAppUpdateManager(getApplicationContext());
        playStoreAppUpdateRequestor = new PlayStoreAppUpdateRequestor(fakeAppUpdateManager);
        playStoreAppUpdateRequestor.addObserver(mockedObserver);
        playStoreAppUpdateRequestor.onCreate();
    }

    @Test
    public void testRequestingAppUpdateFails() {

        AppUpdateRequest mockedAppUpdateRequest = mock(AppUpdateRequest.class);

        playStoreAppUpdateRequestor.requestAppUpdate(mockedAppUpdateRequest);

        verify(mockedObserver, times(1)).onRequestAppUpdateFailure(any(RuntimeException.class));
    }

    @Test
    public void testUserDoesNotAcceptImmediateUpdate() {

        makeUpdateAvailable(AppUpdateType.IMMEDIATE);

        playStoreAppUpdateRequestor.requestAppUpdate(createAppUpdateRequest(AppUpdateType.IMMEDIATE));

        assertThat(fakeAppUpdateManager.isImmediateFlowVisible(), equalTo(true));

        fakeAppUpdateManager.userRejectsUpdate();

        assertThat(fakeAppUpdateManager.isImmediateFlowVisible(), equalTo(false));
    }

    private void makeUpdateAvailable(AppUpdateType appUpdateType) {
        fakeAppUpdateManager.setUpdateAvailable(2, appUpdateType.getPlayStoreAppUpdateType());
    }

    private AppUpdateRequest createAppUpdateRequest(AppUpdateType appUpdateType) {
        return new PlayStoreAppUpdateRequest(appUpdateType,
                mockedActivity,
                1,
                new PlayStoreAppUpdateAvailability(fakeAppUpdateManager.getAppUpdateInfo().getResult()));
    }

    @Test
    public void testUserAcceptsImmediateUpdate() {

        makeUpdateAvailable(AppUpdateType.IMMEDIATE);

        playStoreAppUpdateRequestor.requestAppUpdate(createAppUpdateRequest(AppUpdateType.IMMEDIATE));

        assertThat(fakeAppUpdateManager.isImmediateFlowVisible(), equalTo(true));

        fakeAppUpdateManager.userAcceptsUpdate();
        fakeAppUpdateManager.downloadStarts();
        fakeAppUpdateManager.downloadCompletes();
        fakeAppUpdateManager.completeUpdate();
        fakeAppUpdateManager.installCompletes();

        assertThat(fakeAppUpdateManager.isImmediateFlowVisible(), equalTo(false));

        verifyNoInteractions(mockedObserver);
    }

    @Test
    public void testUserDoesNotAcceptFlexibleUpdate() {

        makeUpdateAvailable(AppUpdateType.FLEXIBLE);

        playStoreAppUpdateRequestor.requestAppUpdate(createAppUpdateRequest(AppUpdateType.FLEXIBLE));

        assertThat(fakeAppUpdateManager.isConfirmationDialogVisible(), equalTo(true));

        fakeAppUpdateManager.userRejectsUpdate();

        assertThat(fakeAppUpdateManager.isConfirmationDialogVisible(), equalTo(false));
    }

    @Test
    public void testUserAcceptsFlexibleUpdateDownloadsSuccessfullyInstallsSuccessfully() {

        makeUpdateAvailable(AppUpdateType.FLEXIBLE);

        playStoreAppUpdateRequestor.requestAppUpdate(createAppUpdateRequest(AppUpdateType.FLEXIBLE));
        assertThat(fakeAppUpdateManager.isConfirmationDialogVisible(), equalTo(true));

        fakeAppUpdateManager.userAcceptsUpdate();
        assertThat(fakeAppUpdateManager.isConfirmationDialogVisible(), equalTo(false));
        verify(mockedObserver, times(1)).onAppUpdatePendingDownload();

        fakeAppUpdateManager.downloadStarts();

        fakeAppUpdateManager.setTotalBytesToDownload(10);
        verify(mockedObserver, times(1)).onAppUpdateDownloadProgressChange(0, 10);

        fakeAppUpdateManager.setBytesDownloaded(5);
        verify(mockedObserver, times(1)).onAppUpdateDownloadProgressChange(5, 10);

        fakeAppUpdateManager.downloadCompletes();
        verify(mockedObserver, times(1)).onAppUpdateDownloaded();

        fakeAppUpdateManager.completeUpdate();
        verify(mockedObserver, times(1)).onInstallingAppUpdate();

        fakeAppUpdateManager.installCompletes();
        verify(mockedObserver, times(1)).onAppUpdateInstalled();
    }

    @Test
    public void testUserAcceptsFlexibleUpdateDownloadsSuccessfullyInstallFails() {

        makeUpdateAvailable(AppUpdateType.FLEXIBLE);

        playStoreAppUpdateRequestor.requestAppUpdate(createAppUpdateRequest(AppUpdateType.FLEXIBLE));
        assertThat(fakeAppUpdateManager.isConfirmationDialogVisible(), equalTo(true));

        fakeAppUpdateManager.userAcceptsUpdate();
        assertThat(fakeAppUpdateManager.isConfirmationDialogVisible(), equalTo(false));

        fakeAppUpdateManager.downloadStarts();

        fakeAppUpdateManager.setTotalBytesToDownload(10);
        verify(mockedObserver, times(1)).onAppUpdateDownloadProgressChange(0, 10);

        fakeAppUpdateManager.setBytesDownloaded(5);
        verify(mockedObserver, times(1)).onAppUpdateDownloadProgressChange(5, 10);

        fakeAppUpdateManager.downloadCompletes();
        verify(mockedObserver, times(1)).onAppUpdateDownloaded();

        fakeAppUpdateManager.completeUpdate();
        verify(mockedObserver, times(1)).onInstallingAppUpdate();

        fakeAppUpdateManager.installFails();
        verify(mockedObserver, times(1)).onAppUpdateInstallFailure();
    }

    @Test
    public void testUserAcceptsFlexibleUpdateDownloadFails() {

        makeUpdateAvailable(AppUpdateType.FLEXIBLE);

        playStoreAppUpdateRequestor.requestAppUpdate(createAppUpdateRequest(AppUpdateType.FLEXIBLE));
        assertThat(fakeAppUpdateManager.isConfirmationDialogVisible(), equalTo(true));

        fakeAppUpdateManager.userAcceptsUpdate();
        assertThat(fakeAppUpdateManager.isConfirmationDialogVisible(), equalTo(false));

        fakeAppUpdateManager.downloadStarts();
        fakeAppUpdateManager.downloadFails();
        verify(mockedObserver, times(1)).onAppUpdateDownloadFailure();
    }
}
