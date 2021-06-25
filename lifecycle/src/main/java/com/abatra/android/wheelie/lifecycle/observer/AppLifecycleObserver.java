package com.abatra.android.wheelie.lifecycle.observer;

import androidx.lifecycle.ProcessLifecycleOwner;

public interface AppLifecycleObserver extends ILifecycleObserver {

    default void observeAppLifecycle() {
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
    }
}
