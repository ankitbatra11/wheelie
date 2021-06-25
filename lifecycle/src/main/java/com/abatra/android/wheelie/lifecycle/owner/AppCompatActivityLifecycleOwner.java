package com.abatra.android.wheelie.lifecycle.owner;

import android.content.Context;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.ActivityResultRegistry;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;

public class AppCompatActivityLifecycleOwner implements ILifecycleOwner {

    private final AppCompatActivity activity;

    public AppCompatActivityLifecycleOwner(AppCompatActivity activity) {
        this.activity = activity;
    }

    @Override
    public AppCompatActivity getAppCompatActivity() {
        return activity;
    }

    @Override
    public Context getContext() {
        return activity;
    }

    @NonNull
    @Override
    public <I, O> ActivityResultLauncher<I> registerForActivityResult(@NonNull ActivityResultContract<I, O> contract,
                                                                      @NonNull ActivityResultCallback<O> callback) {
        return activity.registerForActivityResult(contract, callback);
    }

    @NonNull
    @Override
    public <I, O> ActivityResultLauncher<I> registerForActivityResult(@NonNull ActivityResultContract<I, O> contract,
                                                                      @NonNull ActivityResultRegistry registry,
                                                                      @NonNull ActivityResultCallback<O> callback) {
        return activity.registerForActivityResult(contract, registry, callback);
    }

    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return activity.getLifecycle();
    }
}
