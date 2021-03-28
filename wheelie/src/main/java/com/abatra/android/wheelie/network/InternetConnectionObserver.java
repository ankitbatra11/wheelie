package com.abatra.android.wheelie.network;

import android.content.Context;
import android.os.Build;

import androidx.lifecycle.LiveData;

import com.abatra.android.wheelie.lifecycle.ILifecycleObserver;

public interface InternetConnectionObserver extends ILifecycleObserver {

    static InternetConnectionObserver newInstance(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return new QOrAboveInternetConnectionObserver(context);
        } else {
            return new PreQInternetConnectionObserver(context);
        }
    }

    LiveData<Boolean> isConnectedToInternet();
}
