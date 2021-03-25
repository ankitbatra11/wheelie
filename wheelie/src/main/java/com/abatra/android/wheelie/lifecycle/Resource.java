package com.abatra.android.wheelie.lifecycle;

import androidx.annotation.Nullable;

import java.util.Optional;

public class Resource<T> {

    @Nullable
    private T data;

    @Nullable
    private Throwable error;

    private final Status status;

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

    public Optional<T> getData() {
        return Optional.ofNullable(data);
    }

    public Optional<Throwable> getError() {
        return Optional.ofNullable(error);
    }

    enum Status {
        LOADING,
        LOADED,
        FAILED
    }
}
