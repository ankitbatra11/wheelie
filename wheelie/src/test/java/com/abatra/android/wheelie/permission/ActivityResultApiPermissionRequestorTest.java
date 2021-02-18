package com.abatra.android.wheelie.permission;

import android.content.ActivityNotFoundException;
import android.content.pm.PackageManager;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Lifecycle;

import com.abatra.android.wheelie.lifecycle.ILifecycleOwner;

import org.hamcrest.MatcherAssert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ActivityResultApiPermissionRequestorTest {

    @Mock
    private AppCompatActivity mockedActivity;

    @Mock
    private ILifecycleOwner mockedLifecycleOwner;

    @InjectMocks
    private ActivityResultApiPermissionRequestor permissionRequestor;

    @Mock
    private Lifecycle mockedLifecycle;

    @Mock
    private PermissionRequestor.SinglePermissionRequestCallback mockedSinglePermissionRequestCallback;

    @Mock
    private ActivityResultLauncher<String> mockedSinglePermissionActivityResultLauncher;

    private MockedStatic<ContextCompat> mockedContextCompat;
    private MockedStatic<ActivityCompat> mockedActivityCompat;

    @Before
    public void setup() {

        when(mockedLifecycleOwner.getLifecycle()).thenReturn(mockedLifecycle);
        when(mockedLifecycleOwner.getActivity()).thenReturn(mockedActivity);
        permissionRequestor.observeLifecycle(mockedLifecycleOwner);

        mockedContextCompat = mockStatic(ContextCompat.class);
        mockedContextCompat.when(() -> ContextCompat.checkSelfPermission(ArgumentMatchers.any(), anyString()))
                .thenReturn(PackageManager.PERMISSION_DENIED);

        mockedActivityCompat = mockStatic(ActivityCompat.class);
        mockedActivityCompat.when(() -> ActivityCompat.shouldShowRequestPermissionRationale(ArgumentMatchers.any(), anyString())).thenReturn(true);
    }

    @After
    public void tearDown() {
        mockedContextCompat.close();
        mockedActivityCompat.close();
    }

    @Test
    public void testObserveLifecycle() {
        assertTrue(permissionRequestor.getLifecycleOwner().isPresent());
        MatcherAssert.assertThat(permissionRequestor.getLifecycleOwner().get(), sameInstance(mockedLifecycleOwner));
        verify(mockedLifecycle, times(1)).addObserver(permissionRequestor);
    }

    @Test
    public void testOnCreate() {

        permissionRequestor.onCreate();

        //noinspection unchecked
        verify(mockedActivity, times(1)).registerForActivityResult(
                ArgumentMatchers.any(ActivityResultContracts.RequestPermission.class),
                ArgumentMatchers.any(ActivityResultCallback.class));
    }

    @Test
    public void testRequestSystemPermission_permissionAlreadyGranted() {

        mockedContextCompat.when(() -> ContextCompat.checkSelfPermission(ArgumentMatchers.any(), anyString()))
                .thenReturn(PackageManager.PERMISSION_GRANTED);

        permissionRequestor.requestSystemPermission("p", mockedSinglePermissionRequestCallback);

        verify(mockedSinglePermissionRequestCallback, times(1)).onPermissionGranted();
        verifyNoMoreInteractions(mockedSinglePermissionRequestCallback);
    }

    @Test
    public void testRequestSystemPermission_permissionDenied_activityNotFoundException() {

        doThrow(new ActivityNotFoundException()).when(mockedSinglePermissionActivityResultLauncher).launch(anyString());
        permissionRequestor.setSinglePermissionActivityResultLauncher(mockedSinglePermissionActivityResultLauncher);

        permissionRequestor.requestSystemPermission("p", mockedSinglePermissionRequestCallback);

        verify(mockedSinglePermissionRequestCallback, times(1)).onPermissionHandlerActivityNotFound();
        verifyNoMoreInteractions(mockedSinglePermissionRequestCallback);
    }

    @Test
    public void testRequestSystemPermission_permissionDenied_permissionGranted() {

        permissionRequestor.requestSystemPermission("p", mockedSinglePermissionRequestCallback);

        permissionRequestor.getSinglePermissionActivityResultCallback().onActivityResult(true);

        verify(mockedSinglePermissionRequestCallback, times(1)).onPermissionGranted();
        verifyNoMoreInteractions(mockedSinglePermissionRequestCallback);
    }

    @Test
    public void testRequestSystemPermission_permissionDenied_permissionDenied() {

        permissionRequestor.requestSystemPermission("p", mockedSinglePermissionRequestCallback);

        permissionRequestor.getSinglePermissionActivityResultCallback().onActivityResult(false);

        verify(mockedSinglePermissionRequestCallback, times(1)).onPermissionDenied();
        verifyNoMoreInteractions(mockedSinglePermissionRequestCallback);
    }

    @Test
    public void testRequestSystemPermission_permissionDenied_permissionDeniedPermanently() {

        mockedActivityCompat.when(() -> ActivityCompat.shouldShowRequestPermissionRationale(ArgumentMatchers.any(), anyString())).thenReturn(false);

        permissionRequestor.requestSystemPermission("p", mockedSinglePermissionRequestCallback);

        permissionRequestor.getSinglePermissionActivityResultCallback().onActivityResult(false);

        verify(mockedSinglePermissionRequestCallback, times(1)).onPermissionPermanentlyDenied();
        verifyNoMoreInteractions(mockedSinglePermissionRequestCallback);
    }

    @Test
    public void testOnDestroy() {

        permissionRequestor.requestSystemPermission("p",mockedSinglePermissionRequestCallback);
        assertTrue(permissionRequestor.getSinglePermissionRequestCallbackDelegator().isPresent());

        assertTrue(permissionRequestor.getLifecycleOwner().isPresent());
        assertTrue(permissionRequestor.getSinglePermissionRequestCallbackDelegator().isPresent());

        permissionRequestor.onDestroy();

        assertFalse(permissionRequestor.getSinglePermissionRequestCallbackDelegator().isPresent());
        assertFalse(permissionRequestor.getLifecycleOwner().isPresent());
        assertFalse(permissionRequestor.getSinglePermissionRequestCallbackDelegator().isPresent());
    }
}
