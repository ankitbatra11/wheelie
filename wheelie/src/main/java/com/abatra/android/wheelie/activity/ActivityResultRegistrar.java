package com.abatra.android.wheelie.activity;

import android.content.Context;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;

public interface ActivityResultRegistrar extends LifecycleOwner {

    <I, O> ActivityResultLauncher<I> registerForActivityResult(@NonNull final ActivityResultContract<I, O> contract,
                                                               @NonNull final ActivityResultCallback<O> callback);

    Context getContext();
}
