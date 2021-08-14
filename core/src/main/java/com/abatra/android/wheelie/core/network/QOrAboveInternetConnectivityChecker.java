package com.abatra.android.wheelie.core.network;

import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.abatra.android.wheelie.core.content.WrappedContext;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import timber.log.Timber;

@RequiresApi(api = Build.VERSION_CODES.Q)
public class QOrAboveInternetConnectivityChecker extends AbstractInternetConnectivityChecker {

    private static final IntentFilter INTENT_FILTER = new IntentFilter();
    private static final String ACTION_CONNECTIVITY_CHANGE = "android.net.conn.CONNECTIVITY_CHANGE";

    static {
        INTENT_FILTER.addAction(ACTION_CONNECTIVITY_CHANGE);
    }

    private static final NetworkRequest NETWORK_REQUEST = new NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build();

    private final WrappedContext wrappedContext;
    private final AtomicInteger activeNetworkCount = new AtomicInteger(0);
    private final ConnectivityManager.NetworkCallback networkCallback = new ConnectivityStatusUpdater();
    private boolean connected = false;

    public QOrAboveInternetConnectivityChecker(WrappedContext wrappedContext) {
        this.wrappedContext = wrappedContext;
    }

    public static QOrAboveInternetConnectivityChecker newInstance(Context context) {
        return new QOrAboveInternetConnectivityChecker(WrappedContext.wrap(context));
    }

    @Override
    public void startChecking() {
        getExtConnectivityManager().ifPresent(manager -> manager.registerNetworkCallback(NETWORK_REQUEST, networkCallback));
    }

    @Override
    public boolean isConnectedToInternet() {
        return connected;
    }

    @Override
    public void stopChecking() {
        getExtConnectivityManager().ifPresent(manager -> manager.unregisterNetworkCallback(networkCallback));
    }

    private Optional<WrappedConnectivityManager> getExtConnectivityManager() {
        return WrappedConnectivityManager.from(wrappedContext.getContext());
    }

    private class ConnectivityStatusUpdater extends ConnectivityManager.NetworkCallback {

        @Override
        public void onUnavailable() {
            super.onUnavailable();
            Timber.v("onUnavailable");
            activeNetworkCount.set(0);
            updateConnectivityStatus();
        }

        private void updateConnectivityStatus() {
            connected = zeroIfNegative(activeNetworkCount.get()) > 0;
            notifyConnectivityChange(connected);
        }

        private Integer zeroIfNegative(int i) {
            return Math.max(0, i);
        }

        @Override
        public void onAvailable(@NonNull Network network) {
            super.onAvailable(network);
            Timber.v("onAvailable network=%s newCount=%d", network, activeNetworkCount.incrementAndGet());
            updateConnectivityStatus();
        }

        @Override
        public void onLost(@NonNull Network network) {
            super.onLost(network);
            Timber.v("onLost network=%s newCount=%d", network, activeNetworkCount.decrementAndGet());
            updateConnectivityStatus();
        }
    }
}
