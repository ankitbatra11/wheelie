package com.abatra.android.wheelie.lifecycle;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

public interface ILifecycleObserver extends LifecycleObserver {

    default void observeLifecycle(ILifecycleOwner lifecycleOwner) {
        lifecycleOwner.getLifecycle().addObserver(this);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    default void onCreate() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    default void onStart() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    default void onResume() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    default void onPause() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    default void onStop() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    default void onDestroy() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
    default void onAnyEvent() {
    }
}
