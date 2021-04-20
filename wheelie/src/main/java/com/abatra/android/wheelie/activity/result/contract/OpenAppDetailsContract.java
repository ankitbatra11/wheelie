package com.abatra.android.wheelie.activity.result.contract;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.abatra.android.wheelie.intent.IntentFactory;

public class OpenAppDetailsContract extends AbstractActivityResultContract<Void> {

    @NonNull
    @Override
    public Intent createIntent(@NonNull Context context, Void input) {
        return IntentFactory.openAppDetailsSettings(context);
    }
}
