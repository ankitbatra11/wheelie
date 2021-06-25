package com.abatra.android.wheelie.core;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.function.Function;

import bolts.Task;

public class Lce<T> {

    public enum Status {
        LOADING,
        LOADED,
        FAILED
    }

    private final Status status;
    private final T data;
    private final Throwable error;

    protected Lce(Status status, @Nullable T data, @Nullable Throwable error) {
        this.status = status;
        this.data = data;
        this.error = error;
    }

    public static <T> Lce<T> from(Task<T> task) {
        if (task.getError() != null) {
            return Lce.failed(task.getError());
        } else {
            return Lce.loaded(task.getResult());
        }
    }

    public static <T> Lce<T> loading() {
        return new Lce<>(Status.LOADING, null, null);
    }

    public static <T> Lce<T> loaded(T data) {
        return new Lce<>(Status.LOADED, data, null);
    }

    public static <T> Lce<T> failed(Throwable error) {
        return new Lce<>(Status.FAILED, null, error);
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

    public <V> Lce<V> map(Function<T, V> function) {
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

    public boolean isLoaded() {
        return status == Status.LOADED;
    }

    public boolean isLoading() {
        return status == Status.LOADING;
    }

    public boolean isFailed() {
        return status == Status.FAILED;
    }

    @NonNull
    @Override
    public String toString() {
        return "Lce{" +
                "status=" + status +
                ", data=" + data +
                ", error=" + error +
                '}';
    }
}
