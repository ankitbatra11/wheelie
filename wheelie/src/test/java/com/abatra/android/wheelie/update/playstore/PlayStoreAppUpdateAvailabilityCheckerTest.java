package com.abatra.android.wheelie.update.playstore;

import android.os.Build;

import com.abatra.android.wheelie.update.AppUpdateAvailability;
import com.abatra.android.wheelie.update.AppUpdateAvailabilityChecker;
import com.abatra.android.wheelie.update.AppUpdateAvailabilityCriteria;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.testing.FakeAppUpdateManager;
import com.google.android.play.core.tasks.OnCompleteListener;
import com.google.android.play.core.tasks.Task;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = Build.VERSION_CODES.P)
public class PlayStoreAppUpdateAvailabilityCheckerTest {

    private PlayStoreAppUpdateAvailabilityChecker playStoreAppUpdateAvailabilityChecker;
    private FakeAppUpdateManager fakeAppUpdateManager;

    @Mock
    private AppUpdateAvailabilityChecker.Callback mockedCallback;

    @Mock
    private AppUpdateAvailabilityCriteria mockedAppUpdateAvailabilityCriteria;

    @Captor
    private ArgumentCaptor<OnCompleteListener<AppUpdateInfo>> appUpdateInfoOnCompleteListener;

    @Captor
    private ArgumentCaptor<AppUpdateAvailability> appUpdateAvailabilityArgumentCaptor;

    @Before
    public void setup() {

        MockitoAnnotations.openMocks(this);

        when(mockedAppUpdateAvailabilityCriteria.meets(any())).thenReturn(true);

        fakeAppUpdateManager = new FakeAppUpdateManager(getApplicationContext());
        playStoreAppUpdateAvailabilityChecker = new PlayStoreAppUpdateAvailabilityChecker(fakeAppUpdateManager);
        playStoreAppUpdateAvailabilityChecker.appUpdateCriteriaCheckExecutor = bolts.Task.UI_THREAD_EXECUTOR;
    }

    @Test
    public void testCheckingAppUpdateFailure() {

        AppUpdateManager mockedAppUpdateManager = mock(AppUpdateManager.class);

        //noinspection unchecked
        Task<AppUpdateInfo> mockedAppUpdateInfoTask = mock(Task.class);
        when(mockedAppUpdateManager.getAppUpdateInfo()).thenReturn(mockedAppUpdateInfoTask);
        when(mockedAppUpdateInfoTask.isSuccessful()).thenReturn(false);

        RuntimeException exception = new RuntimeException("Simulate check update failure!");
        when(mockedAppUpdateInfoTask.getException()).thenReturn(exception);

        playStoreAppUpdateAvailabilityChecker = new PlayStoreAppUpdateAvailabilityChecker(mockedAppUpdateManager);
        playStoreAppUpdateAvailabilityChecker.checkAppUpdateAvailability(mockedAppUpdateAvailabilityCriteria, mockedCallback);

        verify(mockedAppUpdateInfoTask, times(1)).addOnCompleteListener(appUpdateInfoOnCompleteListener.capture());
        assertThat(appUpdateInfoOnCompleteListener.getValue(), notNullValue());
        appUpdateInfoOnCompleteListener.getValue().onComplete(mockedAppUpdateInfoTask);
        verify(mockedAppUpdateInfoTask, times(1)).isSuccessful();
        verify(mockedCallback, times(1)).onAppUpdateAvailableCheckFailed(exception);
    }

    @Test
    public void testCheckingAppUpdate_appUpdateIsAvailable() {

        fakeAppUpdateManager.setUpdateAvailable(2);

        playStoreAppUpdateAvailabilityChecker.checkAppUpdateAvailability(mockedAppUpdateAvailabilityCriteria, mockedCallback);
        Robolectric.flushForegroundThreadScheduler();

        verify(mockedCallback, times(1)).onAppUpdateAvailable(appUpdateAvailabilityArgumentCaptor.capture());

        assertThat(appUpdateAvailabilityArgumentCaptor.getValue(), instanceOf(PlayStoreAppUpdateAvailability.class));
        PlayStoreAppUpdateAvailability storeAppUpdateAvailability =
                (PlayStoreAppUpdateAvailability) appUpdateAvailabilityArgumentCaptor.getValue();

        assertThat(storeAppUpdateAvailability.isUpdateAvailable(), equalTo(true));

    }

    @Test
    public void testCheckingAppUpdate_appUpdateIsNotAvailable() {

        fakeAppUpdateManager.setUpdateNotAvailable();

        playStoreAppUpdateAvailabilityChecker.checkAppUpdateAvailability(mockedAppUpdateAvailabilityCriteria, mockedCallback);
        Robolectric.flushForegroundThreadScheduler();

        verify(mockedCallback, times(1)).onAppUpdateCriteriaNotMet();
        verify(mockedAppUpdateAvailabilityCriteria, never()).meets(any());

    }

    @Test
    public void testCheckingAppUpdate_appUpdateCriteriaNotMet() {

        fakeAppUpdateManager.setUpdateAvailable(2);
        when(mockedAppUpdateAvailabilityCriteria.meets(any())).thenReturn(false);

        playStoreAppUpdateAvailabilityChecker.checkAppUpdateAvailability(mockedAppUpdateAvailabilityCriteria, mockedCallback);
        Robolectric.flushForegroundThreadScheduler();

        verify(mockedCallback, times(1)).onAppUpdateCriteriaNotMet();
        verify(mockedAppUpdateAvailabilityCriteria, times(1)).meets(any(PlayStoreAppUpdateAvailability.class));
    }
}
