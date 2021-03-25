package com.abatra.android.wheelie.lifecycle;

import androidx.lifecycle.MutableLiveData;

import com.abatra.android.wheelie.thread.BoltsUtils;

import java.util.Optional;

import bolts.Task;

class ResourceLiveData<T> implements ResourceLiveDataApi<T> {

    private final MutableLiveData<Resource<T>> liveData;
    private final Resource<T> loading = Resource.loading();

    public ResourceLiveData(MutableLiveData<Resource<T>> liveData) {
        this.liveData = liveData;
    }

    public void setResourceValue(T value) {
        liveData.setValue(Resource.loaded(value));
    }

    public void postResourceValue(T value) {
        liveData.postValue(Resource.loaded(value));
    }

    public void setLoading() {
        liveData.setValue(loading);
    }

    public void postLoading() {
        liveData.postValue(loading);
    }

    public void setError(Throwable error) {
        liveData.setValue(Resource.failed(error));
    }

    public void postError(Throwable error) {
        liveData.postValue(Resource.failed(error));
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
