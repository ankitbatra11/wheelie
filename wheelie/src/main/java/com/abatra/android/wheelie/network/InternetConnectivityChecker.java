package com.abatra.android.wheelie.network;

import android.content.Context;
import android.os.Build;

import androidx.lifecycle.LiveData;

import com.abatra.android.wheelie.lifecycle.observer.ILifecycleObserver;

public interface InternetConnectivityChecker extends ILifecycleObserver {

    static InternetConnectivityChecker newInstance(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return new QOrAboveInternetConnectivityChecker(context);
        } else {
            return new PreQInternetConnectivityChecker(context);
        }
    }

    LiveData<Boolean> isConnectedToInternet();
}
