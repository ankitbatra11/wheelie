package com.abatra.android.wheelie.lifecycle.network;

import android.content.Context;

import com.abatra.android.wheelie.core.network.InternetConnectivityChecker;
import com.abatra.android.wheelie.core.network.InternetConnectivityListener;
import com.abatra.android.wheelie.core.version.AndroidApi;
import com.abatra.android.wheelie.lifecycle.observer.ILifecycleObserver;

import java.util.function.Consumer;

public class LifecycleInternetConnectivityChecker implements InternetConnectivityChecker, ILifecycleObserver {

    private final InternetConnectivityChecker delegate;

    public LifecycleInternetConnectivityChecker(InternetConnectivityChecker delegate) {
        this.delegate = delegate;
    }

    public static LifecycleInternetConnectivityChecker newInstance(Context context) {
        InternetConnectivityChecker delegate = AndroidApi.getInstance().internetConnectivityChecker(context);
        return new LifecycleInternetConnectivityChecker(delegate);
    }

    @Override
    public void onCreate() {
        startChecking();
    }

    @Override
    public void onDestroy() {
        stopChecking();
    }

    @Override
    public void addObserver(InternetConnectivityListener observer) {
        delegate.addObserver(observer);
    }

    @Override
    public void removeObserver(InternetConnectivityListener observer) {
        delegate.removeObserver(observer);
    }

    @Override
    public void forEachObserver(Consumer<InternetConnectivityListener> observerConsumer) {
        delegate.forEachObserver(observerConsumer);
    }

    @Override
    public void removeObservers() {
        delegate.removeObservers();
    }

    @Override
    public void startChecking() {
        delegate.startChecking();
    }

    @Override
    public boolean isConnectedToInternet() {
        return delegate.isConnectedToInternet();
    }

    @Override
    public void stopChecking() {
        delegate.stopChecking();
    }
}
