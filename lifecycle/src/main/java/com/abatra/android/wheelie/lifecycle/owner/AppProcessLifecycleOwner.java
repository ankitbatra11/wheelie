package com.abatra.android.wheelie.lifecycle.owner;

import android.content.Context;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.ActivityResultRegistry;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ProcessLifecycleOwner;

public class AppProcessLifecycleOwner implements ILifecycleOwner {

    private static final AppProcessLifecycleOwner INSTANCE = new AppProcessLifecycleOwner();

    private AppProcessLifecycleOwner() {
    }

    public static AppProcessLifecycleOwner getInstance() {
        return INSTANCE;
    }

    @Override
    public Context getContext() {
        throw new UnsupportedOperationException();
    }

    @NonNull
    @Override
    public <I, O> ActivityResultLauncher<I> registerForActivityResult(@NonNull ActivityResultContract<I, O> contract,
                                                                      @NonNull ActivityResultCallback<O> callback) {
        throw new UnsupportedOperationException();
    }

    @NonNull
    @Override
    public <I, O> ActivityResultLauncher<I> registerForActivityResult(@NonNull ActivityResultContract<I, O> contract,
                                                                      @NonNull ActivityResultRegistry registry,
                                                                      @NonNull ActivityResultCallback<O> callback) {
        throw new UnsupportedOperationException();
    }

    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return ProcessLifecycleOwner.get().getLifecycle();
    }
}
