package com.abatra.android.wheelie.chronicle;

import android.os.Build;

import com.abatra.android.wheelie.chronicle.model.BeginCheckoutEventParams;
import com.abatra.android.wheelie.chronicle.model.PurchaseEventParams;
import com.abatra.android.wheelie.chronicle.model.ScreenViewEventParams;
import com.abatra.android.wheelie.chronicle.model.SelectItemEventParams;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static com.abatra.android.wheelie.chronicle.Chronicle.eventBuilder;
import static com.abatra.android.wheelie.chronicle.Chronicle.record;
import static com.abatra.android.wheelie.chronicle.Chronicle.recordBeginCheckoutEvent;
import static com.abatra.android.wheelie.chronicle.Chronicle.recordEventOfName;
import static com.abatra.android.wheelie.chronicle.Chronicle.recordPurchaseEvent;
import static com.abatra.android.wheelie.chronicle.Chronicle.recordScreenViewEvent;
import static com.abatra.android.wheelie.chronicle.Chronicle.recordSelectItemEvent;
import static com.abatra.android.wheelie.chronicle.Chronicle.setConfig;
import static org.hamcrest.MatcherAssert.assertThat;
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
    private Event mockedEvent;

    @Mock
    private BeginCheckoutEventParams mockedBeginCheckoutEventParams;

    @Mock
    private PurchaseEventParams mockedPurchaseEventParams;

    @Mock
    private UserPropertySetter mockedUserPropertySetter;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
        setConfig(new ChronicleConfig(() -> mockedEventBuilder, mockedEventRecorder, mockedUserPropertySetter));
    }

    @Test
    public void test_eventBuilder() {
        assertThat(eventBuilder(), sameInstance(mockedEventBuilder));
    }

    @Test
    public void test_recordScreenViewEvent() {

        ScreenViewEventParams screenViewEventParams = new ScreenViewEventParams("", "");

        recordScreenViewEvent(screenViewEventParams);

        verify(mockedEventRecorder, timeout(1)).record(null);
        verify(mockedEventBuilder, times(1)).buildScreenViewEvent(screenViewEventParams);
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
    public void test_recordSecondItemEvent() {

        when(mockedEventBuilder.buildSelectItemEvent(ArgumentMatchers.any())).thenReturn(mockedEvent);
        SelectItemEventParams selectItemEventParams = new SelectItemEventParams();

        recordSelectItemEvent(selectItemEventParams);

        verify(mockedEventBuilder, times(1)).buildSelectItemEvent(selectItemEventParams);
        verify(mockedEventRecorder, times(1)).record(mockedEvent);
    }

    @Test
    public void test_setUserProperty() {

        String expectedName = "pName";
        String expectedValue = "pValue";

        Chronicle.setUserProperty(expectedName, expectedValue);

        verify(mockedUserPropertySetter, times(1)).setUserProperty(expectedName, expectedValue);
    }
}
