package com.abatra.android.wheelie.core.network;

import androidx.annotation.CallSuper;

import com.abatra.android.wheelie.core.designpattern.Observable;

abstract public class AbstractInternetConnectivityChecker implements InternetConnectivityChecker {

    private final Observable<InternetConnectivityListener> listeners = Observable.copyOnWriteArraySet();

    @Override
    @CallSuper
    public void addObserver(InternetConnectivityListener observer) {
        listeners.addObserver(observer);
    }

    @Override
    @CallSuper
    public void removeObserver(InternetConnectivityListener observer) {
        listeners.removeObserver(observer);
    }

    @Override
    @CallSuper
    public void removeObservers() {
        listeners.removeObservers();
    }

    protected void notifyConnectivityChange(boolean connected) {
        listeners.forEachObserver(listener -> listener.onConnectivityChange(connected));
    }
}
