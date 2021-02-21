package com.abatra.android.wheelie.thread;

import java.util.Optional;
import java.util.concurrent.Callable;

import bolts.Continuation;
import bolts.Task;
import timber.log.Timber;

public class Bolts {

    private Bolts() {
    }

    public static <V> Task<V> callOnBackgroundThread(Callable<V> callable) {
        return Task.callInBackground(backgroundThreadCallable(callable));
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

    public static <TTaskResult, TContinuationResult> Continuation<TTaskResult, TContinuationResult>
    continueWithBackgroundThread(Continuation<TTaskResult, TContinuationResult> continuation) {
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

    public static <V> Task<Optional<V>> callOnUiThread(Callable<V> callable) {
        return Task.call(uiThreadCallable(callable), Task.UI_THREAD_EXECUTOR);
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

    public static <TTaskResult, TContinuationResult> Continuation<TTaskResult, Optional<TContinuationResult>>
    continueWithUiThread(Continuation<TTaskResult, TContinuationResult> continuation) {
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

    public static <V> Optional<V> getTaskResult(Task<V> task) {
        return Optional.ofNullable(task.getResult());
    }

    public static <V> Optional<V> getTaskOptionalResult(Task<Optional<V>> task) {
        return Optional.ofNullable(task.getResult()).orElse(Optional.empty());
    }

}
