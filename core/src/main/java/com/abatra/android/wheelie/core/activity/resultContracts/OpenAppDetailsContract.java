package com.abatra.android.wheelie.core.activity.resultContracts;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.abatra.android.wheelie.core.content.Intents;

public class OpenAppDetailsContract extends AbstractActivityResultContract<Void> {

    @NonNull
    @Override
    public Intent createIntent(@NonNull Context context, Void input) {
        return Intents.openAppDetailsSettings(context);
    }
}
