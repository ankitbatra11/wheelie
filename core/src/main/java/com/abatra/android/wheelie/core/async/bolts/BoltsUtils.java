package com.abatra.android.wheelie.core.async.bolts;

import java.util.Optional;

import bolts.Task;

public final class BoltsUtils {

    private BoltsUtils() {
    }

    public static <V> Optional<V> getOptionalResultTaskResult(SaferTask<Optional<V>> task) {
        return Optional.ofNullable(task.getResult()).orElse(Optional.empty());
    }

    public static <V> Optional<V> getOptionalResultTaskResult(Task<Optional<V>> task) {
        return getResult(task).orElse(Optional.empty());
    }

    public static <V> Optional<V> getResult(Task<V> task) {
        return Optional.ofNullable(task.getResult());
    }

}
