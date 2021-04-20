package com.abatra.android.wheelie.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkRequest;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.abatra.android.wheelie.context.ExtendedContext;

import timber.log.Timber;

public class ExtConnectivityManager {

    private final ConnectivityManager connectivityManager;

    private ExtConnectivityManager(ConnectivityManager connectivityManager) {
        this.connectivityManager = connectivityManager;
    }

    public static ExtConnectivityManager wrap(ConnectivityManager connectivityManager) {
        return new ExtConnectivityManager(connectivityManager);
    }

    public static ExtConnectivityManager from(Context context) {
        return wrap(ExtendedContext.wrap(context).getSystemService(Context.CONNECTIVITY_SERVICE));
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void registerNetworkCallback(@NonNull NetworkRequest request,
                                        @NonNull ConnectivityManager.NetworkCallback networkCallback) {
        try {
            connectivityManager.registerNetworkCallback(request, networkCallback);
        } catch (Throwable error) {
            Timber.e(error);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void unregisterNetworkCallback(@NonNull ConnectivityManager.NetworkCallback networkCallback) {
        try {
            connectivityManager.unregisterNetworkCallback(networkCallback);
        } catch (Throwable error) {
            Timber.e(error);
        }
    }

    public ConnectivityManager getConnectivityManager() {
        return connectivityManager;
    }
}
