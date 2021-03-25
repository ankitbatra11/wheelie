package com.abatra.android.wheelie.lifecycle;

import androidx.lifecycle.MutableLiveData;

public class ResourceMutableLiveData<T> extends MutableLiveData<Resource<T>> {

    public void setResourceValue(T value) {
        setValue(Resource.loaded(value));
    }

    public void postResourceValue(T value) {
        postValue(Resource.loaded(value));
    }

    public void setLoading() {
        setValue(Resource.loading());
    }

    public void postLoading() {
        postValue(Resource.loading());
    }

    public void setError(Throwable error) {
        setValue(Resource.failed(error));
    }

    public void postError(Throwable error) {
        postValue(Resource.failed(error));
    }
}
