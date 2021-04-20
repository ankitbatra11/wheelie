package com.abatra.android.wheelie.lifecycle;

import androidx.annotation.Nullable;

import com.abatra.android.wheelie.activity.ActivityStarter;
import com.abatra.android.wheelie.lifecycle.observer.ILifecycleObserver;
import com.abatra.android.wheelie.lifecycle.owner.ILifecycleOwner;

import java.util.Optional;

public class LifecycleBoundValue<T> implements ILifecycleObserver {

    @Nullable
    private T value;

    @Override
    public void observeLifecycle(ILifecycleOwner lifecycleOwner) {
        lifecycleOwner.getLifecycle().addObserver(this);
    }

    public void setValue(@Nullable T value) {
        this.value = value;
    }

    public Optional<T> getValue() {
        return Optional.ofNullable(value);
    }

    public T requireValue() {
        return getValue().orElseThrow(() -> new IllegalStateException("Value is null"));
    }

    @Override
    public void onDestroy() {
        value = null;
    }
}
