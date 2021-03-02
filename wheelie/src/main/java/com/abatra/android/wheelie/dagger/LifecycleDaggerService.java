package com.abatra.android.wheelie.dagger;

import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ServiceLifecycleDispatcher;

import dagger.android.DaggerService;

/**
 * {@link DaggerService} equivalent of {@link androidx.lifecycle.LifecycleService}.
 */
public class LifecycleDaggerService extends DaggerService implements LifecycleOwner {

    private final ServiceLifecycleDispatcher lifecycleDispatcher = new ServiceLifecycleDispatcher(this);

    @Override
    public void onCreate() {
        lifecycleDispatcher.onServicePreSuperOnCreate();
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        lifecycleDispatcher.onServicePreSuperOnBind();
        return null;
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onStart(Intent intent, int startId) {
        lifecycleDispatcher.onServicePreSuperOnStart();
        super.onStart(intent, startId);
    }

    /**
     * Call super which invokes {@link #onStart(Intent, int)}.
     */
    @Override
    @CallSuper
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        lifecycleDispatcher.onServicePreSuperOnDestroy();
        super.onDestroy();
    }

    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return lifecycleDispatcher.getLifecycle();
    }
}
