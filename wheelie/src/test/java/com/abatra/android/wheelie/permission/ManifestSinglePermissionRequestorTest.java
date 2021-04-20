package com.abatra.android.wheelie.permission;

import androidx.activity.result.contract.ActivityResultContracts.RequestPermission;
import androidx.lifecycle.Lifecycle;

import com.abatra.android.wheelie.lifecycle.owner.ILifecycleOwner;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ManifestSinglePermissionRequestorTest {

    @InjectMocks
    private ManifestSinglePermissionRequestor permissionRequestor;

    @Mock
    private ILifecycleOwner mockedLifecycleOwner;

    @Mock
    private Lifecycle mockedLifecycle;

    @Before
    public void setup() {
        when(mockedLifecycleOwner.getLifecycle()).thenReturn(mockedLifecycle);
        permissionRequestor.observeLifecycle(mockedLifecycleOwner);
    }

    @Test
    public void test_isPermissionGranted_true() {
        try (MockedStatic<PermissionUtils> permissionUtilsMockedStatic = mockStatic(PermissionUtils.class)) {
            permissionUtilsMockedStatic.when(() -> PermissionUtils.isPermissionGranted(any(), any())).thenReturn(true);
            assertTrue(permissionRequestor.isPermissionGranted("p"));
        }
    }

    @Test
    public void test_isPermissionGranted_false() {
        try (MockedStatic<PermissionUtils> permissionUtilsMockedStatic = mockStatic(PermissionUtils.class)) {
            permissionUtilsMockedStatic.when(() -> PermissionUtils.isPermissionGranted(any(), any())).thenReturn(false);
            assertFalse(permissionRequestor.isPermissionGranted("p"));
        }
    }

    @Test
    public void test_createRequestPermissionActivityResultContract() {
        assertThat(permissionRequestor.createRequestPermissionActivityResultContract(), instanceOf(RequestPermission.class));
    }

    @Test
    public void test_createBuilder() {
        assertThat(permissionRequestor.createBuilder(), instanceOf(ManifestSinglePermissionGrantResultBuilder.class));
    }
}
