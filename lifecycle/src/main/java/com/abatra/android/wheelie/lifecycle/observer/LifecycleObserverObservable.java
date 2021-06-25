package com.abatra.android.wheelie.lifecycle.observer;

import com.abatra.android.wheelie.designPattern.Observable;

public interface LifecycleObserverObservable<O> extends ILifecycleObserver, Observable<O> {

    @Override
    default void addObserver(O observer) {
    }

    @Override
    default void removeObserver(O observer) {
    }

    @Override
    default void onDestroy() {
        removeObservers();
    }
}
