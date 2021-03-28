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

import com.abatra.android.wheelie.thread.BoltsUtils;

import timber.log.Timber;

import static com.abatra.android.wheelie.thread.SaferTask.backgroundTask;

public class PreQInternetConnectionObserver implements InternetConnectionObserver {

    private static final IntentFilter INTENT_FILTER = new IntentFilter();
    private static final String ACTION_CONNECTIVITY_CHANGE = "android.net.conn.CONNECTIVITY_CHANGE";

    static {
        INTENT_FILTER.addAction(ACTION_CONNECTIVITY_CHANGE);
    }

    private final Context context;
    private final MutableLiveData<Boolean> connectedLiveData = new MutableLiveData<>();
    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateConnectedLiveData();
        }
    };

    public PreQInternetConnectionObserver(Context context) {
        this.context = context;
    }

    @Override
    public void onResume() {
        Timber.v("onResume");
        context.registerReceiver(broadcastReceiver, INTENT_FILTER);
    }

    @Override
    public LiveData<Boolean> isConnectedToInternet() {
        updateConnectedLiveData();
        return Transformations.distinctUntilChanged(connectedLiveData);
    }

    private void updateConnectedLiveData() {
        backgroundTask(() ->
        {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
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
        context.unregisterReceiver(broadcastReceiver);
    }
}
