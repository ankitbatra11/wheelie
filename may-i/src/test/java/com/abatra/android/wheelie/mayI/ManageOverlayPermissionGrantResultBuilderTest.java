package com.abatra.android.wheelie.mayI;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.sameInstance;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mockStatic;

@RunWith(MockitoJUnitRunner.class)
public class ManageOverlayPermissionGrantResultBuilderTest {

    @InjectMocks
    private ManageOverlayPermissionGrantResultBuilder manageOverlayPermissionGrantResultBuilder;

    @Test
    public void test_onPermissionGrantResult_canDrawOverlays() {
        try (MockedStatic<PermissionUtils> permissionUtilsMockedStatic = mockStatic(PermissionUtils.class)) {
            permissionUtilsMockedStatic.when(() -> PermissionUtils.canDrawOverlays(any())).thenReturn(false);
            assertThat(manageOverlayPermissionGrantResultBuilder.onPermissionGrantResult(false, null),
                    sameInstance(SinglePermissionGrantResult.DENIED));
        }
    }

    @Test
    public void test_onPermissionGrantResult_cannotDrawOverlays() {
        try (MockedStatic<PermissionUtils> permissionUtilsMockedStatic = mockStatic(PermissionUtils.class)) {
            permissionUtilsMockedStatic.when(() -> PermissionUtils.canDrawOverlays(any())).thenReturn(true);
            assertThat(manageOverlayPermissionGrantResultBuilder.onPermissionGrantResult(false, null),
                    sameInstance(SinglePermissionGrantResult.GRANTED));
        }
    }
}
