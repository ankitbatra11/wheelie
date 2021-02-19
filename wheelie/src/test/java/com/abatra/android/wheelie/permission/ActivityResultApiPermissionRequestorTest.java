package com.abatra.android.wheelie.permission;

import android.content.ActivityNotFoundException;
import android.content.pm.PackageManager;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Lifecycle;

import com.abatra.android.wheelie.lifecycle.ILifecycleOwner;
import com.abatra.android.wheelie.permission.PermissionRequestor.MultiplePermissionsRequestCallback;
import com.google.common.collect.ImmutableMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ActivityResultApiPermissionRequestorTest {

    @Mock
    private ILifecycleOwner mockedLifecycleOwner;

    @InjectMocks
    private ActivityResultApiPermissionRequestor permissionRequestor;

    @Mock
    private Lifecycle mockedLifecycle;

    @Mock
    private PermissionRequestor.SinglePermissionRequestCallback mockedSinglePermissionRequestCallback;

    @Mock
    private ActivityResultLauncher<String> mockedSinglePermissionRequestor;

    @Mock
    private ActivityResultLauncher<String[]> mockedMultiplePermissionsRequestor;

    @Mock
    private MultiplePermissionsRequestCallback mockedMultiplePermissionsRequestCallback;

    @Captor
    private ArgumentCaptor<ActivityResultCallback<Boolean>> singlePermissionActivityResultCallbackArgumentCaptor;

    @Captor
    private ArgumentCaptor<ActivityResultCallback<Map<String, Boolean>>> multiplePermissionsActivityResultCallbackArgumentCaptor;

    @Captor
    private ArgumentCaptor<Map<String, Boolean>> grantResultByPermissionArgumentCaptor;

    private MockedStatic<ContextCompat> mockedContextCompat;
    private MockedStatic<ActivityCompat> mockedActivityCompat;

    @Before
    public void setup() {

        when(mockedLifecycleOwner.getLifecycle()).thenReturn(mockedLifecycle);

        //noinspection unchecked
        when(mockedLifecycleOwner.registerForActivityResult(
                ArgumentMatchers.any(ActivityResultContracts.RequestPermission.class),
                ArgumentMatchers.any(ActivityResultCallback.class)))
                .thenReturn(mockedSinglePermissionRequestor);

        //noinspection unchecked
        when(mockedLifecycleOwner.registerForActivityResult(
                ArgumentMatchers.any(ActivityResultContracts.RequestMultiplePermissions.class),
                ArgumentMatchers.any(ActivityResultCallback.class)))
                .thenReturn(mockedMultiplePermissionsRequestor);

        mockedContextCompat = mockStatic(ContextCompat.class);
        mockedContextCompat.when(() -> ContextCompat.checkSelfPermission(ArgumentMatchers.any(), anyString()))
                .thenReturn(PackageManager.PERMISSION_DENIED);

        mockedActivityCompat = mockStatic(ActivityCompat.class);
        mockedActivityCompat.when(() -> ActivityCompat.shouldShowRequestPermissionRationale(ArgumentMatchers.any(), anyString())).thenReturn(true);

        permissionRequestor.observeLifecycle(mockedLifecycleOwner);

        assertTrue(permissionRequestor.getLifecycleOwner().isPresent());
        assertThat(permissionRequestor.getLifecycleOwner().get(), sameInstance(mockedLifecycleOwner));
        verify(mockedLifecycle, times(1)).addObserver(permissionRequestor);

        permissionRequestor.onCreate();

        verify(mockedLifecycleOwner, times(1)).registerForActivityResult(
                ArgumentMatchers.any(ActivityResultContracts.RequestPermission.class),
                singlePermissionActivityResultCallbackArgumentCaptor.capture());

        assertThat(singlePermissionActivityResultCallbackArgumentCaptor.getAllValues(), hasSize(1));

        verify(mockedLifecycleOwner, times(1)).registerForActivityResult(
                ArgumentMatchers.any(ActivityResultContracts.RequestMultiplePermissions.class),
                multiplePermissionsActivityResultCallbackArgumentCaptor.capture());

        assertThat(multiplePermissionsActivityResultCallbackArgumentCaptor.getAllValues(), hasSize(1));
    }

    @After
    public void tearDown() {
        mockedContextCompat.close();
        mockedActivityCompat.close();
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

        doThrow(new ActivityNotFoundException()).when(mockedSinglePermissionRequestor).launch(anyString());

        permissionRequestor.requestSystemPermission("p", mockedSinglePermissionRequestCallback);

        verify(mockedSinglePermissionRequestCallback, times(1)).onPermissionHandlerActivityNotFound();
        verifyNoMoreInteractions(mockedSinglePermissionRequestCallback);
    }

    @Test
    public void testRequestSystemPermission_permissionDenied_permissionGranted() {

        permissionRequestor.requestSystemPermission("p", mockedSinglePermissionRequestCallback);

        singlePermissionActivityResultCallbackArgumentCaptor.getValue().onActivityResult(true);

        verify(mockedSinglePermissionRequestCallback, times(1)).onPermissionGranted();
        verifyNoMoreInteractions(mockedSinglePermissionRequestCallback);
    }

    @Test
    public void testRequestSystemPermission_permissionDenied_permissionDenied() {

        permissionRequestor.requestSystemPermission("p", mockedSinglePermissionRequestCallback);

        singlePermissionActivityResultCallbackArgumentCaptor.getValue().onActivityResult(false);

        verify(mockedSinglePermissionRequestCallback, times(1)).onPermissionDenied();
        verifyNoMoreInteractions(mockedSinglePermissionRequestCallback);
    }

    @Test
    public void testRequestSystemPermission_permissionDenied_permissionDeniedPermanently_justNow() {

        mockedActivityCompat.when(() -> ActivityCompat.shouldShowRequestPermissionRationale(ArgumentMatchers.any(), anyString())).thenReturn(true);
        permissionRequestor.requestSystemPermission("p", mockedSinglePermissionRequestCallback);

        mockedActivityCompat.when(() -> ActivityCompat.shouldShowRequestPermissionRationale(ArgumentMatchers.any(), anyString())).thenReturn(false);
        singlePermissionActivityResultCallbackArgumentCaptor.getValue().onActivityResult(false);

        verify(mockedSinglePermissionRequestCallback, times(1)).onPermissionPermanentlyDenied(true);
        verifyNoMoreInteractions(mockedSinglePermissionRequestCallback);
    }

    @Test
    public void testRequestSystemPermission_permissionDenied_permissionDeniedPermanently_previously() {

        mockedActivityCompat.when(() -> ActivityCompat.shouldShowRequestPermissionRationale(ArgumentMatchers.any(), anyString())).thenReturn(false);
        permissionRequestor.requestSystemPermission("p", mockedSinglePermissionRequestCallback);

        singlePermissionActivityResultCallbackArgumentCaptor.getValue().onActivityResult(false);

        verify(mockedSinglePermissionRequestCallback, times(1)).onPermissionPermanentlyDenied(false);
        verifyNoMoreInteractions(mockedSinglePermissionRequestCallback);
    }

    @Test
    public void testOnDestroy() {

        assertTrue(permissionRequestor.getLifecycleOwner().isPresent());
        assertThat(permissionRequestor.getSinglePermissionRequestor(), notNullValue());

        permissionRequestor.requestSystemPermission("p", mockedSinglePermissionRequestCallback);
        assertTrue(permissionRequestor.getSinglePermissionRequestCallbackDelegator().isPresent());

        assertTrue(permissionRequestor.getMultiplePermissionsRequestCallback().isPresent());
        assertThat(permissionRequestor.getMultiplePermissionsRequestor(), notNullValue());

        permissionRequestor.onDestroy();

        assertFalse(permissionRequestor.getLifecycleOwner().isPresent());
        assertThat(permissionRequestor.getSinglePermissionRequestor(), nullValue());
        assertFalse(permissionRequestor.getSinglePermissionRequestCallbackDelegator().isPresent());
        assertFalse(permissionRequestor.getMultiplePermissionsRequestCallback().isPresent());
        assertThat(permissionRequestor.getMultiplePermissionsRequestor(), nullValue());
    }

    @Test
    public void testRequestSystemPermissions_allGranted() {

        mockedContextCompat.when(() -> ContextCompat.checkSelfPermission(ArgumentMatchers.any(), anyString()))
                .thenReturn(PackageManager.PERMISSION_GRANTED);

        permissionRequestor.requestSystemPermissions(new String[]{"pa", "pb"}, mockedMultiplePermissionsRequestCallback);

        verify(mockedMultiplePermissionsRequestCallback, times(1)).onPermissionResult(grantResultByPermissionArgumentCaptor.capture());
        verifyNoMoreInteractions(mockedSinglePermissionRequestCallback);
        assertThat(grantResultByPermissionArgumentCaptor.getValue().entrySet(), hasSize(2));
        assertThat(grantResultByPermissionArgumentCaptor.getValue().get("pa"), equalTo(true));
        assertThat(grantResultByPermissionArgumentCaptor.getValue().get("pb"), equalTo(true));

        verifyNoInteractions(mockedMultiplePermissionsRequestor);
    }

    @Test
    public void testRequestSystemPermissions_notAllGranted() {

        String[] permissions = {"pa", "pb"};
        permissionRequestor.requestSystemPermissions(permissions, mockedMultiplePermissionsRequestCallback);

        multiplePermissionsActivityResultCallbackArgumentCaptor.getValue()
                .onActivityResult(ImmutableMap.of("pa", true, "pb", false));

        verify(mockedMultiplePermissionsRequestCallback, times(1)).onPermissionResult(grantResultByPermissionArgumentCaptor.capture());
        verifyNoMoreInteractions(mockedSinglePermissionRequestCallback);
        assertThat(grantResultByPermissionArgumentCaptor.getValue().entrySet(), hasSize(2));
        assertThat(grantResultByPermissionArgumentCaptor.getValue().get("pa"), equalTo(true));
        assertThat(grantResultByPermissionArgumentCaptor.getValue().get("pb"), equalTo(false));

        verify(mockedMultiplePermissionsRequestor, times(1)).launch(permissions);
    }

    @Test
    public void testRequestSystemPermissions_notAllGranted_activityNotFoundException() {

        doThrow(new ActivityNotFoundException()).when(mockedMultiplePermissionsRequestor).launch(ArgumentMatchers.any(String[].class));

        permissionRequestor.requestSystemPermissions(new String[]{"a", "b"}, mockedMultiplePermissionsRequestCallback);

        verify(mockedMultiplePermissionsRequestCallback, times(1)).onPermissionHandlerActivityNotFound();
        verifyNoMoreInteractions(mockedSinglePermissionRequestCallback);
    }
}
