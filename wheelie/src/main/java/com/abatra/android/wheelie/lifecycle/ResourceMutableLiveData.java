package com.abatra.android.wheelie.lifecycle;

import androidx.lifecycle.MutableLiveData;

import com.abatra.android.wheelie.thread.BoltsUtils;

import java.util.Optional;

import bolts.Task;

public class ResourceMutableLiveData<T> extends MutableLiveData<Resource<T>> {

    private final Resource<T> loading = Resource.loading();

    public void setResourceValue(T value) {
        setValue(Resource.loaded(value));
    }

    public void postResourceValue(T value) {
        postValue(Resource.loaded(value));
    }

    public void setLoading() {
        setValue(loading);
    }

    public void postLoading() {
        postValue(loading);
    }

    public void setError(Throwable error) {
        setValue(Resource.failed(error));
    }

    public void postError(Throwable error) {
        postValue(Resource.failed(error));
    }

    public void setResource(Task<T> task) {
        Optional.ofNullable(task.getError()).ifPresent(this::setError);
        BoltsUtils.getResult(task).ifPresent(this::setResourceValue);
    }

    public void postResource(Task<T> task) {
        Optional.ofNullable(task.getError()).ifPresent(this::postError);
        BoltsUtils.getResult(task).ifPresent(this::postResourceValue);
    }
}
