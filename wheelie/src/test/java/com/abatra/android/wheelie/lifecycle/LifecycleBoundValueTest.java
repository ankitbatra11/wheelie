package com.abatra.android.wheelie.lifecycle;

import androidx.lifecycle.Lifecycle;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LifecycleBoundValueTest {

    @InjectMocks
    private LifecycleBoundValue<Integer> lifecycleBoundValue;

    @Mock
    private ILifecycleOwner mockedLifecycleOwner;

    @Mock
    private Lifecycle mockedLifecycle;

    @Before
    public void setup() {

        when(mockedLifecycleOwner.getLifecycle()).thenReturn(mockedLifecycle);

        lifecycleBoundValue.observeLifecycle(mockedLifecycleOwner);
        verify(mockedLifecycle, times(1)).addObserver(lifecycleBoundValue);
    }

    @Test
    public void testSetValue() {

        assertThat(lifecycleBoundValue.getValue().isPresent(), equalTo(false));

        lifecycleBoundValue.setValue(1);
        assertThat(lifecycleBoundValue.requireValue(), equalTo(1));

        lifecycleBoundValue.setValue(null);
        assertThat(lifecycleBoundValue.getValue().isPresent(), equalTo(false));
    }

    @Test
    public void testGetValue() {

        assertThat(lifecycleBoundValue.getValue().isPresent(), equalTo(false));

        lifecycleBoundValue.setValue(1);

        assertThat(lifecycleBoundValue.getValue().isPresent(), equalTo(true));
        assertThat(lifecycleBoundValue.getValue().get(), equalTo(1));
    }

    @Test
    public void testRequireValue() {

        assertThrows(IllegalStateException.class, () -> lifecycleBoundValue.requireValue());

        lifecycleBoundValue.setValue(2);
        assertThat(lifecycleBoundValue.requireValue(), equalTo(2));
    }

    @Test
    public void testOnDestroy() {

        lifecycleBoundValue.setValue(1);
        assertThat(lifecycleBoundValue.getValue().isPresent(), equalTo(true));

        lifecycleBoundValue.onDestroy();

        assertThat(lifecycleBoundValue.getValue().isPresent(), equalTo(false));
    }
}
