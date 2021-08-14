package com.abatra.android.wheelie.core.content;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import timber.log.Timber;

public class WrappedContext {

    public static final Intent INTENT = new Intent();

    private final Context context;

    private WrappedContext(Context context) {
        this.context = context;
    }

    public static WrappedContext wrap(Context context) {
        return new WrappedContext(context);
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
