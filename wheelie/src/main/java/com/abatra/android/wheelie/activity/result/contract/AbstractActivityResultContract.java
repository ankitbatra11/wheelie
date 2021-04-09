package com.abatra.android.wheelie.activity.result.contract;

import android.content.Intent;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.Nullable;

public abstract class AbstractActivityResultContract<I> extends ActivityResultContract<I, ActivityResult> {

    @Override
    public ActivityResult parseResult(int resultCode, @Nullable Intent intent) {
        return new ActivityResult(resultCode, intent);
    }
}
