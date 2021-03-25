package com.abatra.android.wheelie.lifecycle;

import java.util.function.Function;

import bolts.Task;

interface ResourceLiveDataApi<T> {

    void setResourceValue(T value);

    void postResourceValue(T value);

    void setLoading();

    void postLoading();

    void setError(Throwable error);

    void postError(Throwable error);

    void setResource(Task<T> task);

    void postResource(Task<T> task);

    <V> void setResource(Task<V> task, Function<V, T> function);

    <V> void postResource(Task<V> task, Function<V, T> function);
}
