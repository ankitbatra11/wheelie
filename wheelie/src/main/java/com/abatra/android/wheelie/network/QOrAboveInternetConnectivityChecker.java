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
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.abatra.android.wheelie.context.ExtContext;

import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;

import bolts.Task;
import timber.log.Timber;

import static androidx.lifecycle.Transformations.distinctUntilChanged;
import static androidx.lifecycle.Transformations.map;
import static com.abatra.android.wheelie.thread.SaferTask.callOn;

@RequiresApi(api = Build.VERSION_CODES.Q)
public class QOrAboveInternetConnectivityChecker implements InternetConnectivityChecker {

    private static final IntentFilter INTENT_FILTER = new IntentFilter();
    private static final String ACTION_CONNECTIVITY_CHANGE = "android.net.conn.CONNECTIVITY_CHANGE";

    static {
        INTENT_FILTER.addAction(ACTION_CONNECTIVITY_CHANGE);
    }

    private static final NetworkRequest NETWORK_REQUEST = new NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build();

    private final ExtContext extContext;
    private final AtomicInteger activeNetworkCount = new AtomicInteger(0);
    private final MutableLiveData<Integer> connectedNetworkLiveCount = new MutableLiveData<>(0);
    private final ConnectivityManager.NetworkCallback networkCallback = new ConnectivityManager.NetworkCallback() {

        @Override
        public void onUnavailable() {
            super.onUnavailable();
            Timber.v("onUnavailable");
            activeNetworkCount.set(0);
            updateLiveCount();
        }

        private void updateLiveCount() {
            connectedNetworkLiveCount.postValue(makeZeroIfNegative(activeNetworkCount.get()));
        }

        private Integer makeZeroIfNegative(int i) {
            return Math.max(0, i);
        }

        @Override
        public void onAvailable(@NonNull Network network) {
            super.onAvailable(network);
            Timber.v("onAvailable network=%s newCount=%d", network, activeNetworkCount.incrementAndGet());
            updateLiveCount();
        }

        @Override
        public void onLost(@NonNull Network network) {
            super.onLost(network);
            Timber.v("onLost network=%s newCount=%d", network, activeNetworkCount.decrementAndGet());
            updateLiveCount();
        }
    };
    Executor backgroundExecutor = Task.BACKGROUND_EXECUTOR;

    public QOrAboveInternetConnectivityChecker(Context context) {
        this.extContext = ExtContext.wrap(context);
    }

    @Override
    public void onResume() {
        Timber.v("onResume");
        getExtConnectivityManager().registerNetworkCallback(NETWORK_REQUEST, networkCallback);
    }

    @Override
    public void onPause() {
        Timber.v("onPause");
        getExtConnectivityManager().unregisterNetworkCallback(networkCallback);
    }

    private ExtConnectivityManager getExtConnectivityManager() {
        return ExtConnectivityManager.from(extContext.getContext());
    }

    @Override
    public LiveData<Boolean> isConnectedToInternet() {
        callOn(backgroundExecutor, () -> {
            getExtConnectivityManager().getConnectivityManager().requestNetwork(NETWORK_REQUEST, networkCallback);
            return null;
        });
        return distinctUntilChanged(map(connectedNetworkLiveCount, activeNetworkCount -> activeNetworkCount > 0));
    }
}
