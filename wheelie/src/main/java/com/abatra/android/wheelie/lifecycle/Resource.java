package com.abatra.android.wheelie.lifecycle;

import androidx.annotation.Nullable;

import java.util.Optional;
import java.util.function.Function;

public class Resource<T> {

    private final Status status;
    @Nullable
    private final T data;
    @Nullable
    private final Throwable error;

    Resource(Status status, @Nullable T data, @Nullable Throwable error) {
        this.status = status;
        this.data = data;
        this.error = error;
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

    public enum Status {
        LOADING,
        LOADED,
        FAILED
    }
}
