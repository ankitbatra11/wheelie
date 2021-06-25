package com.abatra.android.wheelie.core.async.bolts;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.function.Supplier;

import bolts.Continuation;
import bolts.Task;
import timber.log.Timber;

public class SaferTask<TR> {

    private final Task<TR> task;

    private SaferTask(Task<TR> task) {
        this.task = task;
    }

    public static <TR> SaferTask<TR> callOn(Executor executor, Callable<TR> callable) {
        return new SaferTask<>(Task.call(backgroundThreadCallable(callable), executor));
    }

    public static <TR> SaferTask<TR> backgroundTask(Callable<TR> callable) {
        return new SaferTask<>(Task.callInBackground(backgroundThreadCallable(callable)));
    }

    private static <V> Callable<V> backgroundThreadCallable(Callable<V> callable) {
        return () -> {
            try {
                return callable.call();
            } catch (Exception exception) {
                Timber.e(exception);
                throw exception;
            } catch (Throwable throwable) {
                Timber.e(throwable);
                throw new RuntimeException(throwable);
            }
        };
    }

    public static <TR> SaferTask<Optional<TR>> uiTask(Callable<TR> callable) {
        return new SaferTask<>(Task.call(uiThreadCallable(callable), Task.UI_THREAD_EXECUTOR));
    }

    private static <V> Callable<Optional<V>> uiThreadCallable(Callable<V> callable) {
        return () -> {
            V value = null;
            try {
                value = callable.call();
            } catch (Throwable error) {
                Timber.e(error);
            }
            return Optional.ofNullable(value);
        };
    }

    public <CR> SaferTask<Optional<CR>> continueOnUiThread(Continuation<TR, CR> continuation) {
        return new SaferTask<>(task.continueWith(uiThreadContinuation(continuation), Task.UI_THREAD_EXECUTOR));
    }

    private static <TTaskResult, TContinuationResult> Continuation<TTaskResult, Optional<TContinuationResult>>
    uiThreadContinuation(Continuation<TTaskResult, TContinuationResult> continuation) {
        return task -> {
            TContinuationResult result = null;
            try {
                result = continuation.then(task);
            } catch (Throwable throwable) {
                Timber.e(throwable);
            }
            return Optional.ofNullable(result);
        };
    }

    public <CR> SaferTask<CR> continueOnBackgroundThread(Continuation<TR, CR> continuation) {
        return new SaferTask<>(task.continueWith(backgroundThreadContinuation(continuation), Task.BACKGROUND_EXECUTOR));
    }

    private static <TTaskResult, TContinuationResult> Continuation<TTaskResult, TContinuationResult>
    backgroundThreadContinuation(Continuation<TTaskResult, TContinuationResult> continuation) {
        return task -> {
            try {
                return continuation.then(task);
            } catch (Exception exception) {
                Timber.e(exception);
                throw exception;
            } catch (Throwable throwable) {
                Timber.e(throwable);
                throw new RuntimeException(throwable);
            }
        };
    }

    public void waitForCompletion() throws InterruptedException {
        task.waitForCompletion();
    }

    public Exception getError() {
        return task.getError();
    }

    public TR getResult() {
        return task.getResult();
    }

    public Optional<TR> getOptionalResult() {
        return Optional.ofNullable(getResult());
    }

    public TR getResultOr(TR orElseValue) {
        return getOptionalResult().orElse(orElseValue);
    }

    public TR getResultOrGet(Supplier<TR> supplier) {
        return getOptionalResult().orElseGet(supplier);
    }

    public boolean isCompleted() {
        return task.isCompleted();
    }
}
