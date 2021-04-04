package com.abatra.android.wheelie.lifecycle;

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
public class ResourceTest {

    public static final Object MAPPED_VALUE = new Object();

    @Mock
    private Task<Object> mockedTask;

    @Test
    public void test_loadingFactory() {

        Resource<Object> resource = Resource.loading();

        verifyResourceIsLoading(resource);
    }

    private void verifyResourceIsLoading(Resource<Object> resource) {
        assertThat(resource.getData(), nullValue());
        assertThat(resource.getStatus(), equalTo(Resource.Status.LOADING));
        assertThat(resource.getError(), nullValue());
    }

    @Test
    public void test_loadedFactory() {

        Object data = new Object();
        Resource<Object> resource = Resource.loaded(data);

        verifyResourceIsLoaded(data, resource);
    }

    private void verifyResourceIsLoaded(Object data, Resource<Object> resource) {
        assertThat(resource.getData(), sameInstance(data));
        assertThat(resource.getStatus(), equalTo(Resource.Status.LOADED));
        assertThat(resource.getError(), nullValue());
    }

    @Test
    public void test_failedFactory() {

        RuntimeException error = new RuntimeException();
        Resource<Object> resource = Resource.failed(error);

        verifyResourceIsFailed(error, resource);
    }

    private void verifyResourceIsFailed(RuntimeException error, Resource<Object> resource) {
        assertThat(resource.getData(), nullValue());
        assertThat(resource.getStatus(), equalTo(Resource.Status.FAILED));
        assertThat(resource.getError(), sameInstance(error));
    }

    @Test
    public void test_map() {

        Resource<Object> resource = Resource.loading().map(o -> o);
        verifyResourceIsLoading(resource);

        resource = Resource.loaded(new Object()).map(o -> MAPPED_VALUE);
        verifyResourceIsLoaded(MAPPED_VALUE, resource);

        RuntimeException error = new RuntimeException();
        resource = Resource.failed(error).map(o -> o);
        verifyResourceIsFailed(error, resource);
    }

    @Test
    public void test_from_task_result() {

        Object data = new Object();
        when(mockedTask.getResult()).thenReturn(data);

        verifyResourceIsLoaded(data, Resource.from(mockedTask));
    }

    @Test
    public void test_from_task_error() {

        RuntimeException error = new RuntimeException();
        when(mockedTask.getError()).thenReturn(error);

        verifyResourceIsFailed(error, Resource.from(mockedTask));
    }

    @Test
    public void test_isLoaded() {
        assertThat(Resource.loading().isLoaded(), equalTo(false));
        assertThat(Resource.loaded(null).isLoaded(), equalTo(true));
        assertThat(Resource.failed(null).isLoaded(), equalTo(false));
    }

    @Test
    public void test_isLoading() {
        assertThat(Resource.loading().isLoading(), equalTo(true));
        assertThat(Resource.loaded(null).isLoading(), equalTo(false));
        assertThat(Resource.failed(null).isLoading(), equalTo(false));
    }

    @Test
    public void test_isFailed() {
        assertThat(Resource.loading().isFailed(), equalTo(false));
        assertThat(Resource.loaded(null).isFailed(), equalTo(false));
        assertThat(Resource.failed(null).isFailed(), equalTo(true));
    }
}
