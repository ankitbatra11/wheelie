package com.abatra.android.wheelie.chronicle.firebase;

import android.os.Build;
import android.os.Bundle;

import com.abatra.android.wheelie.chronicle.Event;
import com.abatra.android.wheelie.chronicle.EventParams;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = Build.VERSION_CODES.P)
public class FirebaseEventRecorderTest {

    @Mock
    private Event mockedEvent;

    @Mock
    private EventParams mockedEventParams;

    @Mock
    private Bundle mockedBundle;

    @Mock
    private FirebaseAnalytics mockedFirebaseAnalytics;

    @InjectMocks
    private FirebaseEventRecorder firebaseEventRecorder;

    @Before
    public void setup() {

        MockitoAnnotations.openMocks(this);

        firebaseEventRecorder = new FirebaseEventRecorder(mockedFirebaseAnalytics);

        when(mockedEvent.getEventParams()).thenReturn(mockedEventParams);
        when(mockedEventParams.bundle()).thenReturn(mockedBundle);
    }

    @Test
    public void test_record() {

        String eventName = "testEvent";
        when(mockedEvent.getName()).thenReturn(eventName);

        firebaseEventRecorder.record(mockedEvent);

        //noinspection ResultOfMethodCallIgnored
        verify(mockedEvent, times(1)).getName();
        verify(mockedEventParams, times(1)).bundle();
        // below line fails if all the tests in this file are run through intelliJ
        verify(mockedFirebaseAnalytics, times(1)).logEvent(eventName, mockedBundle);
    }

}
