package com.abatra.android.wheelie.app;

import android.os.Build;

import com.abatra.android.wheelie.startup.ApplicationInitializer;
import com.abatra.android.wheelie.startup.ApplicationInitializers;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.Collections;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = Build.VERSION_CODES.P)
public class WheelieApplicationTest {

    private WheelieApplication wheelieApplication;

    @Mock
    private ApplicationInitializer applicationInitializer;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void test_onCreate_withInitializers() {

        wheelieApplication = new WheelieApplication() {
            @Override
            protected ApplicationInitializers getApplicationInitializers() {
                return new ApplicationInitializers(Collections.singletonList(applicationInitializer));
            }
        };

        wheelieApplication.onCreate();

        verify(applicationInitializer, times(1)).initialize(wheelieApplication);
    }
}
