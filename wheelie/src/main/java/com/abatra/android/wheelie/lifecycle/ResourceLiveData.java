package com.abatra.android.wheelie.lifecycle;

import androidx.lifecycle.MutableLiveData;

import java.util.Optional;
import java.util.function.Function;

import bolts.Task;

import static com.abatra.android.wheelie.thread.BoltsUtils.getResult;

class ResourceLiveData<T> implements ResourceLiveDataApi<T> {

    private final MutableLiveData<Resource<T>> liveData;
    private final Resource<T> loading = Resource.loading();

    public ResourceLiveData(MutableLiveData<Resource<T>> liveData) {
        this.liveData = liveData;
    }

    @Override
    public void setResourceValue(T value) {
        liveData.setValue(Resource.loaded(value));
    }

    @Override
    public void postResourceValue(T value) {
        liveData.postValue(Resource.loaded(value));
    }

    @Override
    public void setLoading() {
        liveData.setValue(loading);
    }

    @Override
    public void postLoading() {
        liveData.postValue(loading);
    }

    @Override
    public void setError(Throwable error) {
        liveData.setValue(Resource.failed(error));
    }

    @Override
    public void postError(Throwable error) {
        liveData.postValue(Resource.failed(error));
    }

    @Override
    public void setResource(Task<T> task) {
        setOptionalError(task);
        getResult(task).ifPresent(this::setResourceValue);
    }

    private void setOptionalError(Task<?> task) {
        Optional.ofNullable(task.getError()).ifPresent(this::setError);
    }

    @Override
    public void postResource(Task<T> task) {
        postOptionalError(task);
        getResult(task).ifPresent(this::postResourceValue);
    }

    private void postOptionalError(Task<?> task) {
        Optional.ofNullable(task.getError()).ifPresent(this::postError);
    }

    @Override
    public <V> void setResource(Task<V> task, Function<V, T> function) {
        setOptionalError(task);
        getResult(task).map(function).ifPresent(this::setResourceValue);
    }

    @Override
    public <V> void postResource(Task<V> task, Function<V, T> function) {
        postOptionalError(task);
        getResult(task).map(function).ifPresent(this::postResourceValue);
    }
}
