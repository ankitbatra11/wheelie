package com.abatra.android.wheelie.lifecycle;

import android.content.Context;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultCaller;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.ActivityResultRegistry;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;

import java.util.Optional;

public interface ILifecycleOwner extends LifecycleOwner, ActivityResultCaller {

    static ILifecycleOwner viewOf(Fragment fragment) {
        return new FragmentViewLifecycleOwner(fragment);
    }

    default Context getContext() {
        return Optional.ofNullable(getFragment())
                .map(Fragment::requireContext)
                .orElseGet(this::getAppCompatActivity);
    }

    default Fragment getFragment() {
        return null;
    }

    default AppCompatActivity getAppCompatActivity() {
        return getFragment() != null
                ? getFragment().getActivity() instanceof AppCompatActivity
                ? (AppCompatActivity) getFragment().getActivity()
                : null
                : null;
    }

    @NonNull
    @Override
    default <I, O> ActivityResultLauncher<I> registerForActivityResult(@NonNull ActivityResultContract<I, O> contract,
                                                                       @NonNull ActivityResultCallback<O> callback) {
        return getActivityResultCaller().registerForActivityResult(contract, callback);
    }

    default ActivityResultCaller getActivityResultCaller() {
        return Optional.ofNullable(getFragment())
                .map(f -> (ActivityResultCaller) f)
                .orElseGet(this::getAppCompatActivity);
    }

    @NonNull
    @Override
    default <I, O> ActivityResultLauncher<I> registerForActivityResult(@NonNull ActivityResultContract<I, O> contract,
                                                                       @NonNull ActivityResultRegistry registry,
                                                                       @NonNull ActivityResultCallback<O> callback) {
        return getActivityResultCaller().registerForActivityResult(contract, registry, callback);
    }
}
