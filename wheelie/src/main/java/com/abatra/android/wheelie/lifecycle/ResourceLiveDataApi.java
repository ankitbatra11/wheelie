package com.abatra.android.wheelie.lifecycle;

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
}
