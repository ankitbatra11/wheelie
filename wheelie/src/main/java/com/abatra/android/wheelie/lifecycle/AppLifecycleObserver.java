package com.abatra.android.wheelie.lifecycle;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

public interface AppLifecycleObserver extends LifecycleObserver {

    default void observeAppLifecycle() {
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    default void onAppCreated() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    default void onAppStarted() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    default void onAppResumed() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    default void onAppPaused() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    default void onAppStopped() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    default void onAppDestroyed() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
    default void onAnyAppEvent() {
    }
}
