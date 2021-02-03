package com.abatra.android.wheelie.activity;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;

public interface ActivityResultRegistrar {
    <I, O> ActivityResultLauncher<I> registerForActivityResult(@NonNull final ActivityResultContract<I, O> contract,
                                                               @NonNull final ActivityResultCallback<O> callback);
}
