package com.abatra.android.wheelie.context;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import timber.log.Timber;

public class ExtContext {

    public static final Intent INTENT = new Intent();

    private final Context context;

    private ExtContext(Context context) {
        this.context = context;
    }

    public static ExtContext wrap(Context context) {
        return new ExtContext(context);
    }

    public Intent registerReceiver(BroadcastReceiver broadcastReceiver, IntentFilter intentFilter) {
        try {
            return context.registerReceiver(broadcastReceiver, intentFilter);
        } catch (Throwable error) {
            Timber.e(error);
        }
        return INTENT;
    }

    public void unregisterReceiver(BroadcastReceiver broadcastReceiver) {
        try {
            context.unregisterReceiver(broadcastReceiver);
        } catch (Throwable error) {
            Timber.e(error);
        }
    }

    public <T> T getSystemService(String serviceName) {
        //noinspection unchecked
        return (T) context.getSystemService(serviceName);
    }

    public Context getContext() {
        return context;
    }
}
