package com.abatra.android.wheelie.activity;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;

import androidx.activity.result.ActivityResultLauncher;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.function.Consumer;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@RunWith(MockitoJUnitRunner.class)
public class SafeActivityStarterTest {

    @Mock
    private Intent mockedIntent;

    @Mock
    private ActivityNotFoundErrorHandler mockedErrorHandler;

    @Mock
    private Consumer<Intent> mockedIntentStarter;

    @Mock
    private ActivityResultLauncher<Intent> mockedActivityResultLauncher;

    private ActivityStarter activityStarter;

    @Before
    public void setup() {
        activityStarter = new SafeActivityStarter(mockedIntentStarter);
        activityStarter.setActivityNotFoundErrorHandler(mockedErrorHandler);
    }

    @Test
    public void test_startActivity_activityNotFound() {

        doThrow(new ActivityNotFoundException()).when(mockedIntentStarter).accept(any());

        activityStarter.startActivity(mockedIntent);

        verify(mockedIntentStarter, times(1)).accept(mockedIntent);
        verify(mockedErrorHandler, times(1)).handleActivityNotFoundError(mockedIntent);
    }

    @Test
    public void test_startActivity_unknownError() {

        doThrow(new IllegalStateException()).when(mockedIntentStarter).accept(any());

        activityStarter.startActivity(mockedIntent);

        verify(mockedIntentStarter, times(1)).accept(mockedIntent);
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

        activityStarter.startActivity(mockedIntent);

        verify(mockedIntentStarter, times(1)).accept(mockedIntent);
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
