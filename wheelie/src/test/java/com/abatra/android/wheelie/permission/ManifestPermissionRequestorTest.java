package com.abatra.android.wheelie.permission;

import com.abatra.android.wheelie.lifecycle.ILifecycleOwner;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@RunWith(MockitoJUnitRunner.class)
public class ManifestPermissionRequestorTest {

    @Mock
    private SinglePermissionRequestor mockedSinglePermissionRequestor;

    @Mock
    private MultiplePermissionsRequestor mockedMultiplePermissionsRequestor;

    @InjectMocks
    private ManifestPermissionRequestor manifestPermissionRequestor;

    @Mock
    private ILifecycleOwner mockedLifecycleOwner;

    @Mock
    private MultiplePermissionsRequestor.Callback mockedMultiplePermissionsRequestorCallback;

    @Mock
    private SinglePermissionRequestor.Callback mockedSinglePermissionRequestorCallback;

    @Test
    public void test_observeLifecycle() {

        manifestPermissionRequestor.observeLifecycle(mockedLifecycleOwner);

        verify(mockedSinglePermissionRequestor, times(1)).observeLifecycle(mockedLifecycleOwner);
        verify(mockedMultiplePermissionsRequestor, times(1)).observeLifecycle(mockedLifecycleOwner);
    }

    @Test
    public void test_requestSystemPermissions() {

        String[] permissions = {"pa", "pb"};

        manifestPermissionRequestor.requestSystemPermissions(permissions, mockedMultiplePermissionsRequestorCallback);

        verify(mockedMultiplePermissionsRequestor, times(1)).requestSystemPermissions(permissions,
                mockedMultiplePermissionsRequestorCallback);

        verifyNoInteractions(mockedSinglePermissionRequestor);
    }

    @Test
    public void test_requestSystemPermission() {

        manifestPermissionRequestor.requestSystemPermission("p", mockedSinglePermissionRequestorCallback);

        verify(mockedSinglePermissionRequestor, times(1)).requestSystemPermission("p", mockedSinglePermissionRequestorCallback);
        verifyNoInteractions(mockedMultiplePermissionsRequestor);
    }
}
