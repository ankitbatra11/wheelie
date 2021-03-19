package com.abatra.android.wheelie.chronicle;

import android.os.Build;

import androidx.fragment.app.Fragment;

import com.abatra.android.wheelie.chronicle.firebase.FirebaseEventBuilder;
import com.abatra.android.wheelie.chronicle.firebase.FirebaseEventRecorder;
import com.abatra.android.wheelie.chronicle.model.BeginCheckoutEventParams;
import com.abatra.android.wheelie.chronicle.model.PurchaseEventParams;
import com.abatra.android.wheelie.chronicle.model.SelectItemEventParams;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static com.abatra.android.wheelie.chronicle.Chronicle.eventBuilder;
import static com.abatra.android.wheelie.chronicle.Chronicle.eventRecorder;
import static com.abatra.android.wheelie.chronicle.Chronicle.initializeForFirebase;
import static com.abatra.android.wheelie.chronicle.Chronicle.record;
import static com.abatra.android.wheelie.chronicle.Chronicle.recordBeginCheckoutEvent;
import static com.abatra.android.wheelie.chronicle.Chronicle.recordEventOfName;
import static com.abatra.android.wheelie.chronicle.Chronicle.recordPurchaseEvent;
import static com.abatra.android.wheelie.chronicle.Chronicle.recordScreenViewEvent;
import static com.abatra.android.wheelie.chronicle.Chronicle.recordSelectItemEvent;
import static com.abatra.android.wheelie.chronicle.Chronicle.setConfig;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.sameInstance;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = Build.VERSION_CODES.P)
public class ChronicleTest {

    @Mock
    private EventRecorder mockedEventRecorder;

    @Mock
    private EventBuilder<?> mockedEventBuilder;

    @Mock
    private Fragment mockedFragment;

    @Mock
    private Event mockedEvent;

    @Mock
    private BeginCheckoutEventParams mockedBeginCheckoutEventParams;

    @Mock
    private PurchaseEventParams mockedPurchaseEventParams;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
        setConfig(new ChronicleConfig(getApplicationContext(), () -> mockedEventBuilder, context -> mockedEventRecorder));
    }

    @Test
    public void test_eventBuilder() {
        assertThat(eventBuilder(), sameInstance(mockedEventBuilder));
    }

    @Test
    public void test_recordScreenViewEvent() {

        recordScreenViewEvent(mockedFragment);

        verify(mockedEventRecorder, timeout(1)).record(null);
        verify(mockedEventBuilder, times(1)).buildScreenViewEvent(mockedFragment);
    }

    @Test
    public void test_recordEventOfName() {

        String testEvent = "testEvent";

        recordEventOfName(testEvent);

        verify(mockedEventBuilder, times(1)).buildEventOfName(testEvent);
        verify(mockedEventRecorder, times(1)).record(null);
    }

    @Test
    public void test_record() {

        record(mockedEvent);

        verify(mockedEventRecorder, times(1)).record(mockedEvent);
        verifyNoInteractions(mockedEventBuilder);
    }

    @Test
    public void test_recordBeginCheckoutEvent() {

        recordBeginCheckoutEvent(mockedBeginCheckoutEventParams);

        verify(mockedEventBuilder, times(1)).buildBeginCheckoutEvent(mockedBeginCheckoutEventParams);
        verify(mockedEventRecorder, times(1)).record(null);
    }

    @Test
    public void test_recordPurchaseEvent() {

        recordPurchaseEvent(mockedPurchaseEventParams);

        verify(mockedEventBuilder, times(1)).buildPurchaseEvent(mockedPurchaseEventParams);
        verify(mockedEventRecorder, times(1)).record(null);
    }

    @Test
    public void test_initializeForFirebase() {

        initializeForFirebase(getApplicationContext());

        assertThat(eventBuilder(), instanceOf(FirebaseEventBuilder.class));
        assertThat(eventRecorder(), instanceOf(FirebaseEventRecorder.class));
    }

    @Test
    public void test_recordSecondItemEvent() {

        when(mockedEventBuilder.buildSelectItemEvent(ArgumentMatchers.any())).thenReturn(mockedEvent);
        SelectItemEventParams selectItemEventParams = new SelectItemEventParams();

        recordSelectItemEvent(selectItemEventParams);

        verify(mockedEventBuilder, times(1)).buildSelectItemEvent(selectItemEventParams);
        verify(mockedEventRecorder, times(1)).record(mockedEvent);
    }
}
