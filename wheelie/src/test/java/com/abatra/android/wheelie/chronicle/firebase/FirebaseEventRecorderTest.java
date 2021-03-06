package com.abatra.android.wheelie.chronicle.firebase;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import com.abatra.android.wheelie.chronicle.Event;
import com.abatra.android.wheelie.chronicle.EventParams;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.sameInstance;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = Build.VERSION_CODES.P)
public class FirebaseEventRecorderTest {

    private static FirebaseAnalytics mockedFirebaseAnalytics;
    private static MockedStatic<FirebaseAnalytics> firebaseAnalyticsMockedStatic;

    @Mock
    private Event mockedEvent;

    @Mock
    private EventParams mockedEventParams;

    @Mock
    private Bundle mockedBundle;

    private FirebaseEventRecorder firebaseEventRecorder;

    @BeforeClass
    public static void setupClass() {
        mockedFirebaseAnalytics = mock(FirebaseAnalytics.class);
        firebaseAnalyticsMockedStatic = mockStatic(FirebaseAnalytics.class);
        firebaseAnalyticsMockedStatic.when(() -> FirebaseAnalytics.getInstance(ArgumentMatchers.any(Context.class)))
                .thenReturn(mockedFirebaseAnalytics);
    }

    @AfterClass
    public static void tearDownClass() {
        firebaseAnalyticsMockedStatic.close();
    }

    @Before
    public void setup() {

        MockitoAnnotations.openMocks(this);

        firebaseEventRecorder = FirebaseEventRecorder.getInstance(getApplicationContext());
        firebaseAnalyticsMockedStatic.verify(() -> FirebaseAnalytics.getInstance(any(Context.class)), times(1));

        when(mockedEvent.getEventParams()).thenReturn(mockedEventParams);
        when(mockedEventParams.bundle()).thenReturn(mockedBundle);
    }

    @Test
    public void test_getInstance() {
        assertThat(FirebaseEventRecorder.getInstance(getApplicationContext()), sameInstance(firebaseEventRecorder));
    }

    @Test
    public void test_record() {

        String eventName = "testEvent";
        when(mockedEvent.getName()).thenReturn(eventName);

        FirebaseEventRecorder.getInstance(getApplicationContext()).record(mockedEvent);

        verify(mockedEvent, times(1)).getName();
        verify(mockedEventParams, times(1)).bundle();
        // below line fails if all the tests in this file are run through intelliJ
        verify(mockedFirebaseAnalytics, times(1)).logEvent(eventName, mockedBundle);
    }

}
