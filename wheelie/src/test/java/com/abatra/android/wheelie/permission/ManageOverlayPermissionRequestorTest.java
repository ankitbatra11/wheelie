package com.abatra.android.wheelie.permission;

import android.os.Build;
import android.provider.Settings;

import androidx.lifecycle.Lifecycle;

import com.abatra.android.wheelie.activity.result.RequestManageOverlayPermission;
import com.abatra.android.wheelie.lifecycle.ILifecycleOwner;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = Build.VERSION_CODES.P)
public class ManageOverlayPermissionRequestorTest {

    @InjectMocks
    private ManageOverlayPermissionRequestor permissionRequestor;

    @Mock
    private ILifecycleOwner mockedLifecycleOwner;

    @Mock
    private Lifecycle mockedLifecycle;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
        when(mockedLifecycleOwner.getLifecycle()).thenReturn(mockedLifecycle);
        permissionRequestor.observeLifecycle(mockedLifecycleOwner);
    }

    @Test
    public void test_isPermissionGranted_true_marshmallowOrAbove() {
        try (MockedStatic<Settings> settingsMockedStatic = mockStatic(Settings.class)) {
            settingsMockedStatic.when(() -> Settings.canDrawOverlays(any())).thenReturn(true);
            assertTrue(permissionRequestor.isPermissionGranted("p"));
        }
    }

    @Test
    @Config(sdk = Build.VERSION_CODES.LOLLIPOP)
    public void test_isPermissionGranted_true() {
        try (MockedStatic<Settings> settingsMockedStatic = mockStatic(Settings.class)) {
            settingsMockedStatic.verifyNoInteractions();
            assertTrue(permissionRequestor.isPermissionGranted("p"));
        }
    }

    @Test
    public void test_isPermissionGranted_false() {
        try (MockedStatic<PermissionUtils> permissionUtilsMockedStatic = mockStatic(PermissionUtils.class)) {
            permissionUtilsMockedStatic.when(() -> PermissionUtils.canDrawOverlays(any())).thenReturn(false);
            assertFalse(permissionRequestor.isPermissionGranted("p"));
        }
    }

    @Test
    public void test_createRequestPermissionActivityResultContract() {
        assertThat(permissionRequestor.createRequestPermissionActivityResultContract(),
                instanceOf(RequestManageOverlayPermission.class));
    }

    @Test
    public void test_createBuilder() {
        assertThat(permissionRequestor.createBuilder(), sameInstance(ManageOverlayPermissionGrantResultBuilder.INSTANCE));
    }
}
