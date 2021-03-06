package com.abatra.android.wheelie.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.abatra.android.wheelie.core.async.bolts.BoltsUtils;
import com.abatra.android.wheelie.core.async.bolts.SaferTask;
import com.abatra.android.wheelie.core.content.ExtendedContext;

import java.util.concurrent.Executor;

import bolts.Task;

public class PreQInternetConnectivityChecker extends AbstractInternetConnectivityChecker {

    private static final IntentFilter INTENT_FILTER = new IntentFilter();
    private static final String ACTION_CONNECTIVITY_CHANGE = "android.net.conn.CONNECTIVITY_CHANGE";

    static {
        INTENT_FILTER.addAction(ACTION_CONNECTIVITY_CHANGE);
    }

    private final ExtendedContext extendedContext;
    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateIsConnectedLiveData();
        }
    };
    Executor backgroundExecutor = Task.BACKGROUND_EXECUTOR;

    public PreQInternetConnectivityChecker(Context context) {
        this.extendedContext = ExtendedContext.wrap(context);
    }

    @Override
    protected void startListening() {
        extendedContext.registerReceiver(broadcastReceiver, INTENT_FILTER);
    }

    @Override
    protected void updateIsConnectedLiveData() {
        SaferTask.callOn(backgroundExecutor, () ->
        {
            ConnectivityManager connectivityManager = extendedContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
            return activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        }).continueOnUiThread(task ->
        {
            BoltsUtils.getResult(task).ifPresent(isConnectedLiveData::setValue);
            return null;
        });
    }

    @Override
    protected void stopListening() {
        extendedContext.unregisterReceiver(broadcastReceiver);
    }
}
