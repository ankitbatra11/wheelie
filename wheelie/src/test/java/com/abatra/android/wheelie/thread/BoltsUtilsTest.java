package com.abatra.android.wheelie.thread;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.sameInstance;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BoltsUtilsTest {

    @Mock
    private SaferTask<Optional<Object>> mockedSaferTask;

    @Test
    public void test_getOptionalResultTaskResult_taskResultIsNull_emptyOptionalShouldBeReturned() {

        when(mockedSaferTask.getResult()).thenReturn(null);

        assertThat(BoltsUtils.getOptionalResultTaskResult(mockedSaferTask).isPresent(), equalTo(false));
    }

    @Test
    public void test_getOptionalResultTaskResult_taskResultIsNotNull_taskResultShouldBeReturned() {

        Optional<Object> result = Optional.of(new Object());
        when(mockedSaferTask.getResult()).thenReturn(result);

        assertThat(BoltsUtils.getOptionalResultTaskResult(mockedSaferTask), sameInstance(result));
    }
}
