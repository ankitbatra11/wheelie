package com.abatra.android.wheelie.core;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import bolts.Task;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.sameInstance;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LceTest {

    public static final Object MAPPED_VALUE = new Object();

    @Mock
    private Task<Object> mockedTask;

    @Test
    public void test_loadingFactory() {

        Lce<Object> lce = Lce.loading();

        verifyResourceIsLoading(lce);
    }

    private void verifyResourceIsLoading(Lce<Object> lce) {
        assertThat(lce.getData(), nullValue());
        assertThat(lce.getStatus(), equalTo(Lce.Status.LOADING));
        assertThat(lce.getError(), nullValue());
    }

    @Test
    public void test_loadedFactory() {

        Object data = new Object();
        Lce<Object> lce = Lce.loaded(data);

        verifyResourceIsLoaded(data, lce);
    }

    private void verifyResourceIsLoaded(Object data, Lce<Object> lce) {
        assertThat(lce.getData(), sameInstance(data));
        assertThat(lce.getStatus(), equalTo(Lce.Status.LOADED));
        assertThat(lce.getError(), nullValue());
    }

    @Test
    public void test_failedFactory() {

        RuntimeException error = new RuntimeException();
        Lce<Object> lce = Lce.failed(error);

        verifyResourceIsFailed(error, lce);
    }

    private void verifyResourceIsFailed(RuntimeException error, Lce<Object> lce) {
        assertThat(lce.getData(), nullValue());
        assertThat(lce.getStatus(), equalTo(Lce.Status.FAILED));
        assertThat(lce.getError(), sameInstance(error));
    }

    @Test
    public void test_map() {

        Lce<Object> lce = Lce.loading().map(o -> o);
        verifyResourceIsLoading(lce);

        lce = Lce.loaded(new Object()).map(o -> MAPPED_VALUE);
        verifyResourceIsLoaded(MAPPED_VALUE, lce);

        RuntimeException error = new RuntimeException();
        lce = Lce.failed(error).map(o -> o);
        verifyResourceIsFailed(error, lce);
    }

    @Test
    public void test_from_task_result() {

        Object data = new Object();
        when(mockedTask.getResult()).thenReturn(data);

        verifyResourceIsLoaded(data, Lce.from(mockedTask));
    }

    @Test
    public void test_from_task_error() {

        RuntimeException error = new RuntimeException();
        when(mockedTask.getError()).thenReturn(error);

        verifyResourceIsFailed(error, Lce.from(mockedTask));
    }

    @Test
    public void test_isLoaded() {
        assertThat(Lce.loading().isLoaded(), equalTo(false));
        assertThat(Lce.loaded(null).isLoaded(), equalTo(true));
        assertThat(Lce.failed(null).isLoaded(), equalTo(false));
    }

    @Test
    public void test_isLoading() {
        assertThat(Lce.loading().isLoading(), equalTo(true));
        assertThat(Lce.loaded(null).isLoading(), equalTo(false));
        assertThat(Lce.failed(null).isLoading(), equalTo(false));
    }

    @Test
    public void test_isFailed() {
        assertThat(Lce.loading().isFailed(), equalTo(false));
        assertThat(Lce.loaded(null).isFailed(), equalTo(false));
        assertThat(Lce.failed(null).isFailed(), equalTo(true));
    }
}
