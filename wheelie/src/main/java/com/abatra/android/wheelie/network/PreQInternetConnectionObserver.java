package com.abatra.android.wheelie.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.abatra.android.wheelie.pattern.Observable;

public class PreQInternetConnectionObserver implements InternetConnectionObserver {

    private static final IntentFilter INTENT_FILTER = new IntentFilter();
    private static final String ACTION_CONNECTIVITY_CHANGE = "android.net.conn.CONNECTIVITY_CHANGE";

    static {
        INTENT_FILTER.addAction(ACTION_CONNECTIVITY_CHANGE);
    }

    private final Context context;
    private final Observable<Listener> listeners = Observable.copyOnWriteArraySet();
    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean connectedToInternet = isConnectedToInternet();
            listeners.forEachObserver(listener -> listener.onInternetConnectivityChanged(connectedToInternet));
        }
    };

    public PreQInternetConnectionObserver(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate() {
        context.registerReceiver(broadcastReceiver, INTENT_FILTER);
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
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    @Override
    public void isConnectedToInternet(Listener listener) {
        listener.onInternetConnectivityChanged(isConnectedToInternet());
    }

    @Override
    public void onDestroy() {
        context.unregisterReceiver(broadcastReceiver);
    }
}
