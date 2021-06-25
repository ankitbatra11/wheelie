package com.abatra.android.wheelie.lifecycle.owner;

import android.content.Context;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.ActivityResultRegistry;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;

public class FragmentLifecycleOwner implements ILifecycleOwner {

    private final Fragment fragment;

    public FragmentLifecycleOwner(Fragment fragment) {
        this.fragment = fragment;
    }

    @Override
    public Fragment getFragment() {
        return fragment;
    }

    @Override
    public AppCompatActivity getAppCompatActivity() {
        return (AppCompatActivity) fragment.requireActivity();
    }

    @Override
    public Context getContext() {
        return fragment.requireContext();
    }

    @NonNull
    @Override
    public <I, O> ActivityResultLauncher<I> registerForActivityResult(@NonNull ActivityResultContract<I, O> contract,
                                                                      @NonNull ActivityResultCallback<O> callback) {
        return fragment.registerForActivityResult(contract, callback);
    }

    @NonNull
    @Override
    public <I, O> ActivityResultLauncher<I> registerForActivityResult(@NonNull ActivityResultContract<I, O> contract,
                                                                      @NonNull ActivityResultRegistry registry,
                                                                      @NonNull ActivityResultCallback<O> callback) {
        return fragment.registerForActivityResult(contract, registry, callback);
    }

    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return fragment.getLifecycle();
    }
}
