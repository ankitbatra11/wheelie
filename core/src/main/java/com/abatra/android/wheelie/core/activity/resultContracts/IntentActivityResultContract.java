package com.abatra.android.wheelie.core.activity.resultContracts;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

public class IntentActivityResultContract extends AbstractActivityResultContract<Intent> {

    public static IntentActivityResultContract INSTANCE = new IntentActivityResultContract();

    private IntentActivityResultContract() {
    }

    @NonNull
    @Override
    public Intent createIntent(@NonNull Context context, Intent input) {
        return input;
    }
}
