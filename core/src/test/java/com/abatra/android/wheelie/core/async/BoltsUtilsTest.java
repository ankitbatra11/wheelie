package com.abatra.android.wheelie.core.async;

import com.abatra.android.wheelie.core.async.bolts.SaferTask;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import bolts.Task;

import static com.abatra.android.wheelie.core.async.bolts.BoltsUtils.getOptionalResultTaskResult;
import static com.abatra.android.wheelie.core.async.bolts.BoltsUtils.getResult;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.sameInstance;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BoltsUtilsTest {

    @Mock
    private SaferTask<Optional<Object>> mockedSaferTask;

    @Mock
    private Task<Optional<Object>> mockedOptionalTask;

    @Mock
    private Task<Object> mockedObjectTask;

    @Test
    public void test_getOptionalResultTaskResult_taskResultIsNull_emptyOptionalShouldBeReturned() {

        when(mockedSaferTask.getResult()).thenReturn(null);

        assertThat(getOptionalResultTaskResult(mockedSaferTask).isPresent(), equalTo(false));
    }

    @Test
    public void test_getOptionalResultTaskResult_taskResultIsNotNull_taskResultShouldBeReturned() {

        Optional<Object> result = Optional.of(new Object());
        when(mockedSaferTask.getResult()).thenReturn(result);

        assertThat(getOptionalResultTaskResult(mockedSaferTask), sameInstance(result));
    }

    @Test
    public void test_getOptionalResultTaskResult_task() {

        Optional<Object> empty = Optional.empty();
        when(mockedOptionalTask.getResult()).thenReturn(empty);
        assertThat(getOptionalResultTaskResult(mockedOptionalTask), sameInstance(empty));

        when(mockedOptionalTask.getResult()).thenReturn(Optional.of(1));
        assertThat(getOptionalResultTaskResult(mockedOptionalTask).isPresent(), equalTo(true));
        assertThat(getOptionalResultTaskResult(mockedOptionalTask).get().toString(), equalTo("1"));
    }

    @Test
    public void test_getResult() {

        assertThat(getResult(mockedObjectTask).isPresent(), equalTo(false));

        String result = "result";
        when(mockedObjectTask.getResult()).thenReturn(result);

        assertThat(getResult(mockedObjectTask).isPresent(), equalTo(true));
        assertThat(getResult(mockedObjectTask).get(), equalTo(result));
    }
}
