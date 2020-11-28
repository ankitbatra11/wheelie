package com.abatra.android.wheelie.lifecycle;

import androidx.lifecycle.ProcessLifecycleOwner;

public interface AppLifecycleObserver extends ILifecycleObserver {

    default void observeAppLifecycle() {
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
    }
}
