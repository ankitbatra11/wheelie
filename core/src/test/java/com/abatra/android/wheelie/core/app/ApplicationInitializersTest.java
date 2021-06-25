package com.abatra.android.wheelie.core.app;

import android.app.Application;

import com.abatra.android.wheelie.core.startup.ApplicationInitializer;
import com.abatra.android.wheelie.core.startup.ApplicationInitializers;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ApplicationInitializersTest {

    public static final RuntimeException ERROR = new RuntimeException();

    private ApplicationInitializers applicationInitializers;

    @Mock
    private ApplicationInitializer successApplicationInitializer;

    @Mock
    private ApplicationInitializer failingApplicationInitializer;

    @Mock
    private Application mockedApplication;

    @Before
    public void setup() {
        doThrow(ERROR).when(failingApplicationInitializer).initialize(any());
        applicationInitializers = new ApplicationInitializers(Arrays.asList(successApplicationInitializer, failingApplicationInitializer));
    }

    @Test
    public void testInitialize() {

        applicationInitializers.initialize(mockedApplication);

        verify(successApplicationInitializer, times(1)).initialize(mockedApplication);
        verify(failingApplicationInitializer, times(1)).initialize(mockedApplication);
    }
}
