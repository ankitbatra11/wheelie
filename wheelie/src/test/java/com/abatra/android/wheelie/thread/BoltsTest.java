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

import static com.abatra.android.wheelie.thread.Bolts.callOnBackgroundThread;
import static com.abatra.android.wheelie.thread.Bolts.callOnUiThread;
import static com.abatra.android.wheelie.thread.Bolts.continueWithBackgroundThread;
import static com.abatra.android.wheelie.thread.Bolts.continueWithUiThread;
import static com.abatra.android.wheelie.thread.Bolts.getTaskOptionalResult;
import static com.abatra.android.wheelie.thread.Bolts.getTaskResult;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.sameInstance;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = Build.VERSION_CODES.P)
public class BoltsTest {

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

        Task<Object> objectTask = callOnBackgroundThread(mockedCallable);
        objectTask.waitForCompletion();

        assertThat(objectTask.getError(), instanceOf(IllegalArgumentException.class));
        assertThat(objectTask.getResult(), nullValue());
    }

    @Test
    public void test_callOnBackgroundThread_callThrowsErrorIsConverterToException() throws Exception {

        when(mockedCallable.call()).thenThrow(new OutOfMemoryError());

        Task<Object> objectTask = callOnBackgroundThread(mockedCallable);
        objectTask.waitForCompletion();

        assertThat(objectTask.getError(), instanceOf(RuntimeException.class));
        assertThat(objectTask.getError().getCause(), instanceOf(OutOfMemoryError.class));
        assertThat(objectTask.getResult(), nullValue());
    }

    @Test
    public void test_callOnBackgroundThread_callIsSuccessful() throws InterruptedException {

        Task<Object> objectTask = callOnBackgroundThread(mockedCallable);
        objectTask.waitForCompletion();

        assertThat(objectTask.getResult(), sameInstance(mockedObject));
        assertThat(objectTask.getError(), nullValue());
    }

    @Test
    public void test_callOnUiThread_throwableIsCaught() throws Exception {

        when(mockedCallable.call()).thenThrow(new OutOfMemoryError());

        Task<Optional<Object>> objectTask = callOnUiThread(mockedCallable);
        Robolectric.flushForegroundThreadScheduler();

        await().untilAsserted(() -> {
            assertThat(objectTask.isCompleted(), equalTo(true));
            assertThat(objectTask.getResult().isPresent(), equalTo(false));
            assertThat(objectTask.getError(), nullValue());
        });
    }

    @Test
    public void test_callOnUiThread_doesNotThrowException() {

        Task<Optional<Object>> objectTask = callOnUiThread(mockedCallable);
        Robolectric.flushForegroundThreadScheduler();

        await().untilAsserted(() -> {
            assertThat(objectTask.isCompleted(), equalTo(true));
            assertThat(objectTask.getResult().isPresent(), equalTo(true));
            assertThat(objectTask.getResult().get(), sameInstance(mockedObject));
        });
    }

    @Test
    public void test_getTaskResult_withValue() {

        Task<Object> objectTask = callOnBackgroundThread(mockedCallable);

        await().untilAsserted(() -> {
            assertThat(objectTask.isCompleted(), equalTo(true));
            assertThat(getTaskResult(objectTask).isPresent(), equalTo(true));
            assertThat(getTaskResult(objectTask).get(), sameInstance(mockedObject));
        });
    }

    @Test
    public void test_getTaskResult_withoutValue() throws Exception {

        when(mockedCallable.call()).thenThrow(new OutOfMemoryError());

        Task<Object> objectTask = callOnBackgroundThread(mockedCallable);

        await().untilAsserted(() -> {
            assertThat(objectTask.isCompleted(), equalTo(true));
            assertThat(getTaskResult(objectTask).isPresent(), equalTo(false));
        });
    }

    @Test
    public void test_getTaskOptionalResult_withValue() {

        Task<Optional<Object>> objectTask = callOnUiThread(mockedCallable);
        Robolectric.flushForegroundThreadScheduler();

        await().untilAsserted(() -> {
            assertThat(objectTask.isCompleted(), equalTo(true));
            assertThat(getTaskOptionalResult(objectTask).isPresent(), equalTo(true));
            assertThat(getTaskOptionalResult(objectTask).get(), sameInstance(mockedObject));
        });
    }

    @Test
    public void test_getTaskOptionalResult_withoutValue() throws Exception {

        when(mockedCallable.call()).thenThrow(new OutOfMemoryError());

        Task<Optional<Object>> objectTask = callOnUiThread(mockedCallable);
        Robolectric.flushForegroundThreadScheduler();

        await().untilAsserted(() -> {
            assertThat(objectTask.isCompleted(), equalTo(true));
            assertThat(getTaskOptionalResult(objectTask).isPresent(), equalTo(false));
        });
    }

    @Test
    public void test_continueWithBackgroundThread_exceptionIsRethrown() throws Exception {

        //noinspection unchecked
        when(mockedContinuation.then(ArgumentMatchers.any(Task.class))).thenThrow(new IllegalStateException());

        Task<Object> objectTask = callOnBackgroundThread(mockedCallable).continueWith(continueWithBackgroundThread(mockedContinuation));
        objectTask.waitForCompletion();

        assertThat(objectTask.getError(), instanceOf(IllegalStateException.class));
        assertThat(objectTask.getResult(), nullValue());
    }

    @Test
    public void test_continueWithBackgroundThread_errorIsCaughtAndRethrown() throws Exception {

        //noinspection unchecked
        when(mockedContinuation.then(ArgumentMatchers.any(Task.class))).thenThrow(new AssertionError());

        Task<Object> objectTask = callOnBackgroundThread(mockedCallable).continueWith(continueWithBackgroundThread(mockedContinuation));
        objectTask.waitForCompletion();

        assertThat(objectTask.getError(), instanceOf(RuntimeException.class));
        assertThat(objectTask.getError().getCause(), instanceOf(AssertionError.class));
        assertThat(objectTask.getResult(), nullValue());
    }

    @Test
    public void test_continueWithBackgroundThread_noError() throws InterruptedException {

        Task<Object> objectTask = callOnBackgroundThread(mockedCallable).continueWith(continueWithBackgroundThread(mockedContinuation));
        objectTask.waitForCompletion();

        assertThat(objectTask.getError(), nullValue());
        assertThat(objectTask.getResult(), sameInstance(mockedObject));
    }

    @Test
    public void test_continueWithUiThread_thrownErrorIsConsumed() throws Exception {

        //noinspection unchecked
        when(mockedOptionalTaskContinuation.then(ArgumentMatchers.any(Task.class))).thenThrow(new AssertionError());

        Task<Optional<Object>> objectTask = callOnUiThread(mockedCallable)
                .continueWith(continueWithUiThread(mockedOptionalTaskContinuation));

        Robolectric.flushForegroundThreadScheduler();

        assertThat(objectTask.getError(), nullValue());
        assertThat(objectTask.getResult().isPresent(), equalTo(false));
    }

    @Test
    public void test_continueWithUiThread_noError() {

        Task<Optional<Object>> objectTask = callOnUiThread(mockedCallable)
                .continueWith(continueWithUiThread(mockedOptionalTaskContinuation));

        Robolectric.flushForegroundThreadScheduler();

        assertThat(objectTask.getError(), nullValue());
        assertThat(objectTask.getResult().isPresent(), equalTo(true));
        assertThat(objectTask.getResult().get(), sameInstance(mockedObject));
    }
}
