package com.abatra.android.wheelie.lifecycle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.function.Function;

import bolts.Task;

public class Resource<T> {

    public enum Status {
        LOADING,
        LOADED,
        FAILED
    }

    private final Status status;
    @Nullable
    private final T data;
    @Nullable
    private final Throwable error;

    protected Resource(Status status, @Nullable T data, @Nullable Throwable error) {
        this.status = status;
        this.data = data;
        this.error = error;
    }

    public static <T> Resource<T> fromTask(Task<T> task) {
        if (task.getError() != null) {
            return Resource.failed(task.getError());
        } else {
            return Resource.loaded(task.getResult());
        }
    }

    public static <T> Resource<T> loading() {
        return new Resource<>(Status.LOADING, null, null);
    }

    public static <T> Resource<T> loaded(T data) {
        return new Resource<>(Status.LOADED, data, null);
    }

    public static <T> Resource<T> failed(Throwable error) {
        return new Resource<>(Status.FAILED, null, error);
    }

    public Status getStatus() {
        return status;
    }

    public T getData() {
        return data;
    }

    public Throwable getError() {
        return error;
    }

    public <V> Resource<V> map(Function<T, V> function) {
        switch (status) {
            case LOADING:
                return loading();
            case LOADED:
                return loaded(function.apply(getData()));
            case FAILED:
                return failed(getError());
        }
        throw new IllegalStateException("Invalid status=" + status);
    }

    @NonNull
    @Override
    public String toString() {
        return "Resource{" +
                "status=" + status +
                ", data=" + data +
                ", error=" + error +
                '}';
    }
}
