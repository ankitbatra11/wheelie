package com.abatra.android.wheelie.network;

import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.abatra.android.wheelie.pattern.Observable;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@RequiresApi(api = Build.VERSION_CODES.Q)
public class QOrAboveInternetConnectionObserver implements InternetConnectionObserver {

    private static final IntentFilter INTENT_FILTER = new IntentFilter();
    private static final String ACTION_CONNECTIVITY_CHANGE = "android.net.conn.CONNECTIVITY_CHANGE";

    static {
        INTENT_FILTER.addAction(ACTION_CONNECTIVITY_CHANGE);
    }

    private static final NetworkRequest NETWORK_REQUEST = new NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build();

    private final Context context;
    private final Observable<Listener> listeners = Observable.copyOnWriteArraySet();
    private final Set<Network> availableNetworks = new CopyOnWriteArraySet<>();
    private final ConnectivityManager.NetworkCallback networkCallback = new ConnectivityManager.NetworkCallback() {

        @Override
        public void onAvailable(@NonNull Network network) {
            super.onAvailable(network);
            availableNetworks.add(network);
            listeners.forEachObserver(type -> type.onInternetConnectivityChanged(isConnectedToInternet()));
        }

        @Override
        public void onLost(@NonNull Network network) {
            super.onLost(network);
            availableNetworks.remove(network);
            listeners.forEachObserver(type -> type.onInternetConnectivityChanged(isConnectedToInternet()));
        }
    };

    public QOrAboveInternetConnectionObserver(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate() {
        getConnectivityManager().registerNetworkCallback(NETWORK_REQUEST, networkCallback);
    }

    private ConnectivityManager getConnectivityManager() {
        return (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
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
        return !availableNetworks.isEmpty();
    }

    @Override
    public void isConnectedToInternet(Listener listener) {
        getConnectivityManager().requestNetwork(NETWORK_REQUEST, new ConnectivityManager.NetworkCallback() {
            @Override
            public void onUnavailable() {
                super.onUnavailable();
                listener.onInternetConnectivityChanged(false);
            }

            @Override
            public void onAvailable(@NonNull Network network) {
                super.onAvailable(network);
                listener.onInternetConnectivityChanged(true);
            }
        });
    }

    @Override
    public void onDestroy() {

        availableNetworks.clear();
        listeners.removeObservers();

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        connectivityManager.unregisterNetworkCallback(networkCallback);
    }
}
