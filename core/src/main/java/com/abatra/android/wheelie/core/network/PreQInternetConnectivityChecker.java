package com.abatra.android.wheelie.core.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.abatra.android.wheelie.core.async.bolts.BoltsUtils;
import com.abatra.android.wheelie.core.async.bolts.SaferTask;
import com.abatra.android.wheelie.core.content.WrappedContext;

import java.util.concurrent.Executor;

import bolts.Task;

public class PreQInternetConnectivityChecker extends AbstractInternetConnectivityChecker {

    private static final IntentFilter INTENT_FILTER = new IntentFilter();
    private static final String ACTION_CONNECTIVITY_CHANGE = "android.net.conn.CONNECTIVITY_CHANGE";

    static {
        INTENT_FILTER.addAction(ACTION_CONNECTIVITY_CHANGE);
    }

    private final WrappedContext wrappedContext;
    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateConnectivityStatus();
        }
    };

    Executor backgroundExecutor = Task.BACKGROUND_EXECUTOR;

    public PreQInternetConnectivityChecker(WrappedContext wrappedContext) {
        this.wrappedContext = wrappedContext;
    }

    public static PreQInternetConnectivityChecker newInstance(Context context) {
        return new PreQInternetConnectivityChecker(WrappedContext.wrap(context));
    }

    private void updateConnectivityStatus() {
        SaferTask.callOn(backgroundExecutor, () ->
        {
            ConnectivityManager connectivityManager = wrappedContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
            return activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        }).continueOnUiThread(task ->
        {
            BoltsUtils.getResult(task).ifPresent(this::notifyConnectivityChange);
            return null;
        });
    }

    @Override
    public boolean isConnectedToInternet() {
        return WrappedConnectivityManager.from(wrappedContext.getContext())
                .map(WrappedConnectivityManager::isConnectedToInternet)
                .orElse(false);
    }

    @Override
    public void startChecking() {
        wrappedContext.registerReceiver(broadcastReceiver, INTENT_FILTER);
    }

    @Override
    public void stopChecking() {
        wrappedContext.unregisterReceiver(broadcastReceiver);
    }
}
