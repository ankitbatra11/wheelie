package com.abatra.android.wheelie.thread;

import android.os.Build;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.Optional;
import java.util.concurrent.Callable;

import bolts.Continuation;
import bolts.Task;

import static org.awaitility.Awaitility.await;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.sameInstance;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = Build.VERSION_CODES.P)
public class SaferTaskTest {

    @Mock
    private Callable<Object> mockedCallable;

    @Mock
    private Continuation<Object, Object> mockedContinuation;

    @Mock
    private Continuation<Optional<Object>, Object> mockedOptionalTaskContinuation;

    @Mock
    private Object mockedObject;

    @Before
    public void setup() throws Exception {

        MockitoAnnotations.openMocks(this);

        when(mockedCallable.call()).thenReturn(mockedObject);
        //noinspection unchecked
        when(mockedContinuation.then(ArgumentMatchers.any(Task.class))).thenReturn(mockedObject);
        //noinspection unchecked
        when(mockedOptionalTaskContinuation.then(ArgumentMatchers.any(Task.class))).thenReturn(mockedObject);
    }

    @Test
    public void test_callOnBackgroundThread_callThrowsExceptionIsRethrown() throws Exception {

        when(mockedCallable.call()).thenThrow(new IllegalArgumentException());

        SaferTask<Object> objectTask = SaferTask.backgroundTask(mockedCallable);
        objectTask.waitForCompletion();

        assertThat(objectTask.getError(), instanceOf(IllegalArgumentException.class));
        assertThat(objectTask.getResult(), nullValue());
    }

    @Test
    public void test_callOnBackgroundThread_callThrowsErrorIsConverterToException() throws Exception {

        when(mockedCallable.call()).thenThrow(new OutOfMemoryError());

        SaferTask<Object> objectTask = SaferTask.backgroundTask(mockedCallable);
        objectTask.waitForCompletion();

        assertThat(objectTask.getError(), instanceOf(RuntimeException.class));
        assertThat(objectTask.getError().getCause(), instanceOf(OutOfMemoryError.class));
        assertThat(objectTask.getResult(), nullValue());
    }

    @Test
    public void test_callOnBackgroundThread_callIsSuccessful() throws InterruptedException {

        SaferTask<Object> objectTask = SaferTask.backgroundTask(mockedCallable);
        objectTask.waitForCompletion();

        assertThat(objectTask.getResult(), sameInstance(mockedObject));
        assertThat(objectTask.getError(), nullValue());
    }

    @Test
    public void test_callOnUiThread_throwableIsCaught() throws Exception {

        when(mockedCallable.call()).thenThrow(new OutOfMemoryError());

        SaferTask<Optional<Object>> objectTask = SaferTask.uiTask(mockedCallable);
        Robolectric.flushForegroundThreadScheduler();

        await().untilAsserted(() -> {
            assertThat(objectTask.isCompleted(), equalTo(true));
            assertThat(objectTask.getResult().isPresent(), equalTo(false));
            assertThat(objectTask.getError(), nullValue());
        });
    }

    @Test
    public void test_callOnUiThread_doesNotThrowException() {

        SaferTask<Optional<Object>> objectTask = SaferTask.uiTask(mockedCallable);
        Robolectric.flushForegroundThreadScheduler();

        await().untilAsserted(() -> {
            assertThat(objectTask.isCompleted(), equalTo(true));
            assertThat(objectTask.getResult().isPresent(), equalTo(true));
            assertThat(objectTask.getResult().get(), sameInstance(mockedObject));
        });
    }

    @Test
    public void test_getTaskResult_withValue() {

        SaferTask<Object> objectTask = SaferTask.backgroundTask(mockedCallable);

        await().untilAsserted(() -> {
            assertThat(objectTask.isCompleted(), equalTo(true));
            assertThat(objectTask.getOptionalResult().isPresent(), equalTo(true));
            assertThat(objectTask.getOptionalResult().get(), sameInstance(mockedObject));
        });
    }

    @Test
    public void test_getTaskResult_withoutValue() throws Exception {

        when(mockedCallable.call()).thenThrow(new OutOfMemoryError());

        SaferTask<Object> objectTask = SaferTask.backgroundTask(mockedCallable);

        await().untilAsserted(() -> {
            assertThat(objectTask.isCompleted(), equalTo(true));
            assertThat(objectTask.getOptionalResult().isPresent(), equalTo(false));
        });
    }

    @Test
    public void test_getTaskOptionalResult_withValue() {

        SaferTask<Optional<Object>> objectTask = SaferTask.uiTask(mockedCallable);
        Robolectric.flushForegroundThreadScheduler();

        await().untilAsserted(() -> {
            assertThat(objectTask.isCompleted(), equalTo(true));
            assertThat(objectTask.getOptionalResult().isPresent(), equalTo(true));
            assertThat(objectTask.getOptionalResult().get().isPresent(), equalTo(true));
            assertThat(objectTask.getOptionalResult().get().get(), sameInstance(mockedObject));
        });
    }

    @Test
    public void test_getTaskOptionalResult_withoutValue() throws Exception {

        when(mockedCallable.call()).thenThrow(new OutOfMemoryError());

        SaferTask<Optional<Object>> objectTask = SaferTask.uiTask(mockedCallable);
        Robolectric.flushForegroundThreadScheduler();

        await().untilAsserted(() -> {
            assertThat(objectTask.isCompleted(), equalTo(true));
            assertThat(objectTask.getOptionalResult().isPresent(), equalTo(true));
            assertThat(objectTask.getOptionalResult().get().isPresent(), equalTo(false));
        });
    }

    @Test
    public void test_continueWithBackgroundThread_exceptionIsRethrown() throws Exception {

        //noinspection unchecked
        when(mockedContinuation.then(ArgumentMatchers.any(Task.class))).thenThrow(new IllegalStateException());

        SaferTask<Object> objectTask = SaferTask.backgroundTask(mockedCallable).continueOnBackgroundThread(mockedContinuation);
        objectTask.waitForCompletion();

        assertThat(objectTask.getError(), instanceOf(IllegalStateException.class));
        assertThat(objectTask.getResult(), nullValue());
    }

    @Test
    public void test_continueWithBackgroundThread_errorIsCaughtAndRethrown() throws Exception {

        //noinspection unchecked
        when(mockedContinuation.then(ArgumentMatchers.any(Task.class))).thenThrow(new AssertionError());

        SaferTask<Object> objectTask = SaferTask.backgroundTask(mockedCallable).continueOnBackgroundThread(mockedContinuation);
        objectTask.waitForCompletion();

        assertThat(objectTask.getError(), instanceOf(RuntimeException.class));
        assertThat(objectTask.getError().getCause(), instanceOf(AssertionError.class));
        assertThat(objectTask.getResult(), nullValue());
    }

    @Test
    public void test_continueWithBackgroundThread_noError() throws InterruptedException {

        SaferTask<Object> objectTask = SaferTask.backgroundTask(mockedCallable).continueOnBackgroundThread(mockedContinuation);
        objectTask.waitForCompletion();

        assertThat(objectTask.getError(), nullValue());
        assertThat(objectTask.getResult(), sameInstance(mockedObject));
    }

    @Test
    public void test_continueWithUiThread_thrownErrorIsConsumed() throws Exception {

        //noinspection unchecked
        when(mockedOptionalTaskContinuation.then(ArgumentMatchers.any(Task.class))).thenThrow(new AssertionError());

        SaferTask<Optional<Object>> objectTask = SaferTask.uiTask(mockedCallable).continueOnUiThread(mockedOptionalTaskContinuation);

        Robolectric.flushForegroundThreadScheduler();

        assertThat(objectTask.getError(), nullValue());
        assertThat(objectTask.getResult().isPresent(), equalTo(false));
    }

    @Test
    public void test_continueWithUiThread_noError() {

        SaferTask<Optional<Object>> objectTask = SaferTask.uiTask(mockedCallable).continueOnUiThread(mockedOptionalTaskContinuation);

        Robolectric.flushForegroundThreadScheduler();

        assertThat(objectTask.getError(), nullValue());
        assertThat(objectTask.getResult().isPresent(), equalTo(true));
        assertThat(objectTask.getResult().get(), sameInstance(mockedObject));
    }

    @Test
    public void test_getResultOr() throws Exception {

        when(mockedCallable.call()).thenReturn(null);

        SaferTask<Object> task = SaferTask.backgroundTask(mockedCallable);
        task.waitForCompletion();

        assertThat(task.getResultOr(mockedObject), sameInstance(mockedObject));
    }

    @Test
    public void test_getResultOrGet() throws Exception {

        when(mockedCallable.call()).thenReturn(null);

        SaferTask<Object> task = SaferTask.backgroundTask(mockedCallable);
        task.waitForCompletion();

        assertThat(task.getResultOrGet(() -> mockedObject), sameInstance(mockedObject));
    }
}
