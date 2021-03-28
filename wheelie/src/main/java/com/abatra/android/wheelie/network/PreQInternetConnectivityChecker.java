package com.abatra.android.wheelie.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.abatra.android.wheelie.context.ExtContext;
import com.abatra.android.wheelie.thread.BoltsUtils;

import java.util.concurrent.Executor;

import bolts.Task;
import timber.log.Timber;

import static com.abatra.android.wheelie.thread.SaferTask.callOn;

public class PreQInternetConnectivityChecker implements InternetConnectivityChecker {

    private static final IntentFilter INTENT_FILTER = new IntentFilter();
    private static final String ACTION_CONNECTIVITY_CHANGE = "android.net.conn.CONNECTIVITY_CHANGE";

    static {
        INTENT_FILTER.addAction(ACTION_CONNECTIVITY_CHANGE);
    }

    private final ExtContext extContext;
    private final MutableLiveData<Boolean> connectedLiveData = new MutableLiveData<>();
    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateConnectedLiveData();
        }
    };
    Executor backgroundExecutor = Task.BACKGROUND_EXECUTOR;

    public PreQInternetConnectivityChecker(Context context) {
        this.extContext = ExtContext.wrap(context);
    }

    @Override
    public void onResume() {
        Timber.v("onResume");
        extContext.registerReceiver(broadcastReceiver, INTENT_FILTER);
    }

    @Override
    public LiveData<Boolean> isConnectedToInternet() {
        updateConnectedLiveData();
        return Transformations.distinctUntilChanged(connectedLiveData);
    }

    private void updateConnectedLiveData() {
        callOn(backgroundExecutor, () ->
        {
            ConnectivityManager connectivityManager = extContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
            return activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        }).continueOnUiThread(task ->
        {
            BoltsUtils.getResult(task).ifPresent(connectedLiveData::setValue);
            return null;
        });
    }

    @Override
    public void onPause() {
        Timber.v("onPause");
        extContext.unregisterReceiver(broadcastReceiver);
    }
}
