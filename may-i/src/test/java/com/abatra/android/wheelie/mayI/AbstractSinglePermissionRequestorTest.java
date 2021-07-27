package com.abatra.android.wheelie.mayI;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;

import com.abatra.android.wheelie.lifecycle.owner.ILifecycleOwner;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AbstractSinglePermissionRequestorTest {

    @Mock
    private ILifecycleOwner mockedLifecycleOwner;

    @Mock
    private AppCompatActivity mockedAppCompatActivity;

    @Mock
    private Lifecycle mockedLifecycle;

    @Mock
    private SinglePermissionGrantResult.Builder mockedBuilder;

    @Mock
    private ActivityResultContract<String, Boolean> mockedActivityResultContract;

    private TestSinglePermissionRequestor permissionRequestor;

    @Mock
    private SinglePermissionRequestor.Callback mockedCallback;

    @Mock
    private ActivityResultLauncher<String> mockedSinglePermissionRequestor;

    @Mock
    private SinglePermissionGrantResult mockedSinglePermissionGrantResult;

    @Captor
    private ArgumentCaptor<ActivityResultCallback<Boolean>> singlePermissionActivityResultCallbackArgumentCaptor;

    @Captor
    private ArgumentCaptor<SinglePermissionGrantResult> singlePermissionGrantResultArgumentCaptor;

    @Before
    public void setup() {

        permissionRequestor = new TestSinglePermissionRequestor(false);

        when(mockedLifecycleOwner.getLifecycle()).thenReturn(mockedLifecycle);
        when(mockedLifecycleOwner.getAppCompatActivity()).thenReturn(mockedAppCompatActivity);

        //noinspection unchecked
        when(mockedLifecycleOwner.registerForActivityResult(eq(mockedActivityResultContract), any(ActivityResultCallback.class)))
                .thenReturn(mockedSinglePermissionRequestor);

        permissionRequestor.observeLifecycle(mockedLifecycleOwner);

        assertThat(permissionRequestor.getLifecycleOwner().isPresent(), equalTo(true));
        assertThat(permissionRequestor.getLifecycleOwner().get(), sameInstance(mockedLifecycleOwner));
        verify(mockedLifecycle, times(1)).addObserver(permissionRequestor);

        verify(mockedLifecycleOwner, times(1)).registerForActivityResult(same(mockedActivityResultContract),
                singlePermissionActivityResultCallbackArgumentCaptor.capture());

        assertThat(singlePermissionActivityResultCallbackArgumentCaptor.getAllValues(), hasSize(1));
        assertThat(permissionRequestor.getSinglePermissionRequestor(), sameInstance(mockedSinglePermissionRequestor));

        when(mockedBuilder.onPermissionGrantResult(anyBoolean(), any(AppCompatActivity.class)))
                .thenReturn(mockedSinglePermissionGrantResult);
    }

    @Test
    public void testRequestSystemPermission_permissionAlreadyGranted() {

        permissionRequestor.isPermissionGranted = true;

        permissionRequestor.requestSystemPermission("p", mockedCallback);

        verify(mockedCallback, times(1)).onPermissionResult(singlePermissionGrantResultArgumentCaptor.capture());
        assertTrue(singlePermissionGrantResultArgumentCaptor.getValue().isPermissionGranted());
        assertFalse(singlePermissionGrantResultArgumentCaptor.getValue().isPermissionPermanentlyDeniedBeforeRequest());
        verifyNoInteractions(mockedSinglePermissionRequestor);
    }

    @Test
    public void testRequestSystemPermission_permissionMissing_granted() {

        permissionRequestor.requestSystemPermission("p", mockedCallback);

        verify(mockedBuilder, times(1)).beforeRequestingPermission("p", mockedAppCompatActivity);
        verify(mockedSinglePermissionRequestor, times(1)).launch("p");

        singlePermissionActivityResultCallbackArgumentCaptor.getValue().onActivityResult(true);

        verify(mockedBuilder, times(1)).onPermissionGrantResult(true, mockedAppCompatActivity);
        verify(mockedCallback, times(1)).onPermissionResult(mockedSinglePermissionGrantResult);
    }

    @Test
    public void testRequestSystemPermission_permissionMissing_notGranted() {

        permissionRequestor.requestSystemPermission("p", mockedCallback);

        verify(mockedBuilder, times(1)).beforeRequestingPermission("p", mockedAppCompatActivity);
        verify(mockedSinglePermissionRequestor, times(1)).launch("p");

        singlePermissionActivityResultCallbackArgumentCaptor.getValue().onActivityResult(false);

        verify(mockedBuilder, times(1)).onPermissionGrantResult(false, mockedAppCompatActivity);
        verify(mockedCallback, times(1)).onPermissionResult(mockedSinglePermissionGrantResult);
    }

    @Test
    public void testOnDestroy() {

        permissionRequestor.requestSystemPermission("p", mockedCallback);
        assertTrue(permissionRequestor.getCallbackDelegator().isPresent());
        assertNotNull(permissionRequestor.getLifecycleOwner());
        assertNotNull(permissionRequestor.getSinglePermissionRequestor());

        permissionRequestor.onDestroy();

        assertFalse(permissionRequestor.getCallbackDelegator().isPresent());
        assertFalse(permissionRequestor.getLifecycleOwner().isPresent());
        assertNull(permissionRequestor.getSinglePermissionRequestor());
    }

    private class TestSinglePermissionRequestor extends AbstractSinglePermissionRequestor {

        private boolean isPermissionGranted;

        private TestSinglePermissionRequestor(boolean isPermissionGranted) {
            this.isPermissionGranted = isPermissionGranted;
        }

        @Override
        protected ActivityResultContract<String, Boolean> createRequestPermissionActivityResultContract() {
            return mockedActivityResultContract;
        }

        @Override
        protected boolean isPermissionGranted(String permission) {
            return isPermissionGranted;
        }

        @Override
        protected SinglePermissionGrantResult.Builder createBuilder() {
            return mockedBuilder;
        }
    }
}
