package com.abatra.android.wheelie.network;

import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkRequest;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.abatra.android.wheelie.pattern.Observable;

import java.util.concurrent.atomic.AtomicBoolean;

@RequiresApi(api = Build.VERSION_CODES.Q)
public class QOrAboveInternetConnectionObserver implements InternetConnectionObserver {

    private static final IntentFilter INTENT_FILTER = new IntentFilter();
    private static final String ACTION_CONNECTIVITY_CHANGE = "android.net.conn.CONNECTIVITY_CHANGE";

    static {
        INTENT_FILTER.addAction(ACTION_CONNECTIVITY_CHANGE);
    }

    private final Context context;
    private final Observable<Listener> listeners = Observable.copyOnWriteArraySet();
    private final AtomicBoolean isConnected = new AtomicBoolean(false);
    private final ConnectivityManager.NetworkCallback networkCallback = new ConnectivityManager.NetworkCallback() {
        @Override
        public void onAvailable(@NonNull Network network) {
            super.onAvailable(network);
            isConnected.set(true);
            listeners.forEachObserver(type -> type.onInternetConnectivityChanged(isConnected.get()));
        }

        @Override
        public void onUnavailable() {
            super.onUnavailable();
            isConnected.set(false);
            listeners.forEachObserver(type -> type.onInternetConnectivityChanged(isConnected.get()));
        }
    };

    public QOrAboveInternetConnectionObserver(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        connectivityManager.registerNetworkCallback(new NetworkRequest.Builder().build(), networkCallback);
    }

    @Override
    public void addObserver(Listener observer) {
        listeners.addObserver(observer);
    }

    @Override
    public void removeObserver(Listener observer) {
        listeners.removeObserver(observer);
    }

    @Override
    public boolean isConnectedToInternet() {
        return isConnected.get();
    }

    @Override
    public void onDestroy() {

        listeners.removeObservers();

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        connectivityManager.unregisterNetworkCallback(networkCallback);
    }
}
