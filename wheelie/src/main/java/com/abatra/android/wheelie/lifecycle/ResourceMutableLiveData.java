package com.abatra.android.wheelie.lifecycle;

import androidx.lifecycle.MutableLiveData;

import bolts.Task;

public class ResourceMutableLiveData<T> extends MutableLiveData<Resource<T>> implements ResourceLiveDataApi<T> {

    private final ResourceLiveDataApi<T> delegate = new ResourceLiveData<>(this);

    @Override
    public void setResourceValue(T value) {
        delegate.setResourceValue(value);
    }

    @Override
    public void postResourceValue(T value) {
        delegate.postResourceValue(value);
    }

    @Override
    public void setLoading() {
        delegate.setLoading();
    }

    @Override
    public void postLoading() {
        delegate.postLoading();
    }

    @Override
    public void setError(Throwable error) {
        delegate.setError(error);
    }

    @Override
    public void postError(Throwable error) {
        delegate.postError(error);
    }

    @Override
    public void setResource(Task<T> task) {
        delegate.setResource(task);
    }

    @Override
    public void postResource(Task<T> task) {
        delegate.postResource(task);
    }
}
