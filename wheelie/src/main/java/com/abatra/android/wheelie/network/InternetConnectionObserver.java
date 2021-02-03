package com.abatra.android.wheelie.network;

import android.content.Context;
import android.os.Build;

import com.abatra.android.wheelie.lifecycle.ILifecycleObserver;
import com.abatra.android.wheelie.pattern.Observable;

public interface InternetConnectionObserver extends ILifecycleObserver, Observable<InternetConnectionObserver.Listener> {

    static InternetConnectionObserver newInstance(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return new QOrAboveInternetConnectionObserver(context);
        } else {
            return new PreQInternetConnectionObserver(context);
        }
    }

    boolean isConnectedToInternet();

    void isConnectedToInternet(Listener listener);

    interface Listener {
        void onInternetConnectivityChanged(boolean connected);
    }
}
