package com.abatra.android.wheelie.lifecycle.liveData;

import androidx.lifecycle.MutableLiveData;

import com.abatra.android.wheelie.core.Lce;
import com.abatra.android.wheelie.core.async.bolts.BoltsUtils;

import java.util.Optional;
import java.util.function.Function;

import bolts.Task;

class LceLiveData<T> implements LceLiveDataApi<T> {

    private final MutableLiveData<Lce<T>> liveData;
    private final Lce<T> loading = Lce.loading();

    public LceLiveData(MutableLiveData<Lce<T>> liveData) {
        this.liveData = liveData;
    }

    @Override
    public void setResourceValue(T value) {
        liveData.setValue(Lce.loaded(value));
    }

    @Override
    public void postResourceValue(T value) {
        liveData.postValue(Lce.loaded(value));
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
        liveData.setValue(Lce.failed(error));
    }

    @Override
    public void postError(Throwable error) {
        liveData.postValue(Lce.failed(error));
    }

    @Override
    public void setResource(Task<T> task) {
        setOptionalError(task);
        BoltsUtils.getResult(task).ifPresent(this::setResourceValue);
    }

    private void setOptionalError(Task<?> task) {
        Optional.ofNullable(task.getError()).ifPresent(this::setError);
    }

    @Override
    public void postResource(Task<T> task) {
        postOptionalError(task);
        BoltsUtils.getResult(task).ifPresent(this::postResourceValue);
    }

    private void postOptionalError(Task<?> task) {
        Optional.ofNullable(task.getError()).ifPresent(this::postError);
    }

    @Override
    public <V> void setResource(Task<V> task, Function<V, T> function) {
        setOptionalError(task);
        BoltsUtils.getResult(task).map(function).ifPresent(this::setResourceValue);
    }

    @Override
    public <V> void postResource(Task<V> task, Function<V, T> function) {
        postOptionalError(task);
        BoltsUtils.getResult(task).map(function).ifPresent(this::postResourceValue);
    }
}
