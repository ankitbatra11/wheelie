package com.abatra.android.wheelie.thread;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.function.Supplier;

import bolts.Continuation;
import bolts.Task;
import timber.log.Timber;

public final class BoltsUtils {

    private BoltsUtils() {
    }

    public static <V> Optional<V> getOptionalResultTaskResult(SaferTask<Optional<V>> task) {
        return Optional.ofNullable(task.getResult()).orElse(Optional.empty());
    }

}
