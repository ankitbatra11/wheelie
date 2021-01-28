package com.abatra.android.wheelie.chameleon;

import android.content.Context;

import com.abatra.android.wheelie.chameleon.firebase.FirebaseDynamicConfig;
import com.google.firebase.FirebaseApp;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

public interface DynamicConfig {

    static DynamicConfig initializedFirebase(Context context) {
        FirebaseApp.initializeApp(context);
        return new FirebaseDynamicConfig(FirebaseRemoteConfig.getInstance());
    }

    void initialize(DynamicConfigSettings settings);

    String getStringValue(String key);

    long getLongValue(String key);

    void fetchAndActivate();
}
