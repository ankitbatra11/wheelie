package com.abatra.android.wheelie.activity;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;

import androidx.activity.result.ActivityResultLauncher;
import androidx.fragment.app.Fragment;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@RunWith(MockitoJUnitRunner.class)
public class ErrorHandlingActivityStarterTest {

    @Mock
    private Intent mockedIntent;

    @Mock
    private StartActivityErrorHandler mockedErrorHandler;

    @Mock
    private Context mockedContext;

    @Mock
    private Fragment mockedFragment;

    @Mock
    private Activity mockedActivity;

    @Mock
    private ActivityResultLauncher<Intent> mockedActivityResultLauncher;

    @InjectMocks
    private ErrorHandlingActivityStarter activityStarter;

    @Test
    public void test_startActivity_activityNotFound() {

        doThrow(new ActivityNotFoundException()).when(mockedContext).startActivity(any());

        activityStarter.startActivity(mockedContext, mockedIntent, mockedErrorHandler);

        verify(mockedErrorHandler, times(1)).onActivityNotFoundError(any(ActivityNotFoundException.class));
    }

    @Test
    public void test_startActivity_unknownError() {

        doThrow(new IllegalStateException()).when(mockedFragment).startActivity(any());

        activityStarter.startActivity(mockedFragment, mockedIntent, mockedErrorHandler);

        verify(mockedErrorHandler, times(1)).onError(any(IllegalStateException.class));
    }

    @Test
    public void test_launch_unknownError() {

        doThrow(new IllegalStateException()).when(mockedActivityResultLauncher).launch(any());

        activityStarter.launch(mockedActivityResultLauncher, mockedIntent, mockedErrorHandler);

        verify(mockedErrorHandler, times(1)).onError(any(IllegalStateException.class));
    }

    @Test
    public void test_startActivity_activityFound() {

        activityStarter.startActivity(mockedActivity, mockedIntent, mockedErrorHandler);

        verify(mockedActivity, times(1)).startActivity(mockedIntent);
        verifyNoInteractions(mockedErrorHandler);
    }

    @Test
    public void test_launch_activityNotFound() {

        doThrow(new ActivityNotFoundException()).when(mockedActivityResultLauncher).launch(any());

        activityStarter.launch(mockedActivityResultLauncher, mockedIntent, mockedErrorHandler);

        verify(mockedActivityResultLauncher, times(1)).launch(mockedIntent);
        verify(mockedErrorHandler, times(1)).onActivityNotFoundError(any(ActivityNotFoundException.class));
    }

    @Test
    public void test_launch_activityFound() {

        activityStarter.launch(mockedActivityResultLauncher, mockedIntent, mockedErrorHandler);

        verify(mockedActivityResultLauncher, times(1)).launch(mockedIntent);
        verifyNoInteractions(mockedErrorHandler);
    }
}
