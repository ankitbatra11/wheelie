package com.abatra.android.wheelie.core.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.abatra.android.wheelie.core.content.WrappedContext;

import java.util.Optional;

import timber.log.Timber;

public class WrappedConnectivityManager {

    private final ConnectivityManager connectivityManager;

    private WrappedConnectivityManager(ConnectivityManager connectivityManager) {
        this.connectivityManager = connectivityManager;
    }

    public static WrappedConnectivityManager wrap(ConnectivityManager connectivityManager) {
        return new WrappedConnectivityManager(connectivityManager);
    }

    public static Optional<WrappedConnectivityManager> from(Context context) {
        ConnectivityManager connectivityManager = WrappedContext.wrap(context).getSystemService(Context.CONNECTIVITY_SERVICE);
        return Optional.ofNullable(connectivityManager)
                .map(WrappedConnectivityManager::wrap);
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

    public boolean isConnectedToInternet() {
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
}
