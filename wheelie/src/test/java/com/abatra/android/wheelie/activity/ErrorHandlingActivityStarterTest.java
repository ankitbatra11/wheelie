package com.abatra.android.wheelie.activity;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;

import androidx.activity.result.ActivityResultLauncher;
import androidx.fragment.app.Fragment;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.function.Consumer;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ErrorHandlingActivityStarterTest {

    @Mock
    private Intent mockedIntent;

    @Mock
    private ActivityNotFoundErrorHandler mockedErrorHandler;

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

    @Before
    public void setup() {
        activityStarter.setActivityNotFoundErrorHandler(mockedErrorHandler);
    }

    @Test
    public void test_startActivity_activityNotFound() {

        doThrow(new ActivityNotFoundException()).when(mockedContext).startActivity(any());

        activityStarter.startActivity(mockedContext, mockedIntent);

        verify(mockedErrorHandler, times(1)).handleActivityNotFoundError(mockedIntent);
    }

    @Test
    public void test_startActivity_unknownError() {

        doThrow(new IllegalStateException()).when(mockedFragment).startActivity(any());

        activityStarter.startActivity(mockedFragment, mockedIntent);

        verify(mockedErrorHandler, never()).handleActivityNotFoundError(any());
    }

    @Test
    public void test_launch_unknownError() {

        doThrow(new IllegalStateException()).when(mockedActivityResultLauncher).launch(any());

        activityStarter.launch(mockedActivityResultLauncher, mockedIntent);

        verify(mockedActivityResultLauncher, times(1)).launch(mockedIntent);
        verify(mockedErrorHandler, never()).handleActivityNotFoundError(any());
    }

    @Test
    public void test_startActivity_activityFound() {

        activityStarter.startActivity(mockedActivity, mockedIntent);

        verify(mockedActivity, times(1)).startActivity(mockedIntent);
        verify(mockedErrorHandler, never()).handleActivityNotFoundError(any());

    }

    @Test
    public void test_launch_activityNotFound() {

        doThrow(new ActivityNotFoundException()).when(mockedActivityResultLauncher).launch(any());

        activityStarter.launch(mockedActivityResultLauncher, mockedIntent);

        verify(mockedActivityResultLauncher, times(1)).launch(mockedIntent);
        verify(mockedErrorHandler, times(1)).handleActivityNotFoundError(mockedIntent);
    }

    @Test
    public void test_launch_activityFound() {

        activityStarter.launch(mockedActivityResultLauncher, mockedIntent);

        verify(mockedActivityResultLauncher, times(1)).launch(mockedIntent);
        verify(mockedErrorHandler, never()).handleActivityNotFoundError(any());
    }
}
