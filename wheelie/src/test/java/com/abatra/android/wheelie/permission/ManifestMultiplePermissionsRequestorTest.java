package com.abatra.android.wheelie.permission;

import android.content.pm.PackageManager;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Lifecycle;

import com.abatra.android.wheelie.lifecycle.owner.ILifecycleOwner;
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
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ManifestMultiplePermissionsRequestorTest {

    @InjectMocks
    private ManifestMultiplePermissionsRequestor permissionRequestor;

    @Mock
    private ILifecycleOwner mockedLifecycleOwner;

    @Mock
    private Lifecycle mockedLifecycle;

    @Mock
    private ActivityResultLauncher<String[]> mockedMultiplePermissionsActivityResultLauncher;

    @Captor
    private ArgumentCaptor<ActivityResultCallback<Map<String, Boolean>>> multiplePermissionsActivityResultCallbackArgumentCaptor;

    @Captor
    private ArgumentCaptor<MultiplePermissionsGrantResult> multiplePermissionsGrantResultArgumentCaptor;

    @Mock
    private MultiplePermissionsRequestor.Callback mockedCallback;

    private MockedStatic<ContextCompat> mockedContextCompat;
    private MockedStatic<ActivityCompat> mockedActivityCompat;

    @Before
    public void setup() {

        when(mockedLifecycleOwner.getLifecycle()).thenReturn(mockedLifecycle);

        //noinspection unchecked
        when(mockedLifecycleOwner.registerForActivityResult(
                ArgumentMatchers.any(ActivityResultContracts.RequestMultiplePermissions.class),
                ArgumentMatchers.any(ActivityResultCallback.class)))
                .thenReturn(mockedMultiplePermissionsActivityResultLauncher);

        mockedContextCompat = mockStatic(ContextCompat.class);
        mockedContextCompat.when(() -> ContextCompat.checkSelfPermission(ArgumentMatchers.any(), anyString()))
                .thenReturn(PackageManager.PERMISSION_DENIED);

        mockedActivityCompat = mockStatic(ActivityCompat.class);
        mockedActivityCompat.when(() -> ActivityCompat.shouldShowRequestPermissionRationale(ArgumentMatchers.any(), anyString())).thenReturn(true);

        permissionRequestor.observeLifecycle(mockedLifecycleOwner);

        assertTrue(permissionRequestor.getLifecycleOwner().isPresent());
        assertThat(permissionRequestor.getLifecycleOwner().get(), sameInstance(mockedLifecycleOwner));
        verify(mockedLifecycle, times(1)).addObserver(permissionRequestor);

        assertNotNull(permissionRequestor.getMultiplePermissionsActivityResultLauncher());

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
    public void testOnDestroy() {

        assertTrue(permissionRequestor.getLifecycleOwner().isPresent());

        permissionRequestor.requestSystemPermissions(new String[]{"a", "b"}, mockedCallback);
        assertTrue(permissionRequestor.getCallbackDelegator().isPresent());

        assertThat(permissionRequestor.getMultiplePermissionsActivityResultLauncher(), notNullValue());

        permissionRequestor.onDestroy();

        assertFalse(permissionRequestor.getLifecycleOwner().isPresent());
        assertFalse(permissionRequestor.getCallbackDelegator().isPresent());
        assertThat(permissionRequestor.getMultiplePermissionsActivityResultLauncher(), nullValue());
    }

    @Test
    public void testRequestSystemPermissions_allGranted() {

        mockedContextCompat.when(() -> ContextCompat.checkSelfPermission(ArgumentMatchers.any(), anyString()))
                .thenReturn(PackageManager.PERMISSION_GRANTED);

        permissionRequestor.requestSystemPermissions(new String[]{"pa", "pb"}, mockedCallback);

        verify(mockedCallback, times(1)).onPermissionResult(multiplePermissionsGrantResultArgumentCaptor.capture());
        verifyNoMoreInteractions(mockedCallback);

        assertTrue(multiplePermissionsGrantResultArgumentCaptor.getValue().getSinglePermissionGrantResult("pa").isPermissionGranted());
        assertTrue(multiplePermissionsGrantResultArgumentCaptor.getValue().getSinglePermissionGrantResult("pb").isPermissionGranted());

        verifyNoInteractions(mockedMultiplePermissionsActivityResultLauncher);
    }

    @Test
    public void testRequestSystemPermissions_notAllGranted() {

        String[] permissions = {"pa", "pb", "pc"};

        mockedActivityCompat.when(() -> ActivityCompat.shouldShowRequestPermissionRationale(ArgumentMatchers.any(), eq("pc"))).thenReturn(false);

        permissionRequestor.requestSystemPermissions(permissions, mockedCallback);

        verify(mockedMultiplePermissionsActivityResultLauncher, times(1)).launch(permissions);

        multiplePermissionsActivityResultCallbackArgumentCaptor.getValue().onActivityResult(ImmutableMap.of("pa", true, "pb", false));

        verify(mockedCallback, times(1)).onPermissionResult(multiplePermissionsGrantResultArgumentCaptor.capture());

        assertTrue(multiplePermissionsGrantResultArgumentCaptor.getValue().getSinglePermissionGrantResult("pa").isPermissionGranted());
        assertFalse(multiplePermissionsGrantResultArgumentCaptor.getValue().getSinglePermissionGrantResult("pa").isPermissionPermanentlyDeniedBeforeRequest());

        assertFalse(multiplePermissionsGrantResultArgumentCaptor.getValue().getSinglePermissionGrantResult("pb").isPermissionGranted());
        assertFalse(multiplePermissionsGrantResultArgumentCaptor.getValue().getSinglePermissionGrantResult("pb").isPermissionPermanentlyDeniedBeforeRequest());

        assertFalse(multiplePermissionsGrantResultArgumentCaptor.getValue().getSinglePermissionGrantResult("pc").isPermissionGranted());
        assertTrue(multiplePermissionsGrantResultArgumentCaptor.getValue().getSinglePermissionGrantResult("pc").isPermissionPermanentlyDeniedBeforeRequest());
    }
}
