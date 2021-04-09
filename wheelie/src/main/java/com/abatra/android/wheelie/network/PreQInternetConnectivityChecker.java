package com.abatra.android.wheelie.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.abatra.android.wheelie.context.ExtContext;
import com.abatra.android.wheelie.thread.BoltsUtils;

import java.util.concurrent.Executor;

import bolts.Task;

import static com.abatra.android.wheelie.thread.SaferTask.callOn;

public class PreQInternetConnectivityChecker extends AbstractInternetConnectivityChecker {

    private static final IntentFilter INTENT_FILTER = new IntentFilter();
    private static final String ACTION_CONNECTIVITY_CHANGE = "android.net.conn.CONNECTIVITY_CHANGE";

    static {
        INTENT_FILTER.addAction(ACTION_CONNECTIVITY_CHANGE);
    }

    private final ExtContext extContext;
    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateIsConnectedLiveData();
        }
    };
    Executor backgroundExecutor = Task.BACKGROUND_EXECUTOR;

    public PreQInternetConnectivityChecker(Context context) {
        this.extContext = ExtContext.wrap(context);
    }

    @Override
    protected void startListening() {
        extContext.registerReceiver(broadcastReceiver, INTENT_FILTER);
    }

    @Override
    protected void updateIsConnectedLiveData() {
        callOn(backgroundExecutor, () ->
        {
            ConnectivityManager connectivityManager = extContext.getSystemService(Context.CONNECTIVITY_SERVICE);
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
        extContext.unregisterReceiver(broadcastReceiver);
    }
}
