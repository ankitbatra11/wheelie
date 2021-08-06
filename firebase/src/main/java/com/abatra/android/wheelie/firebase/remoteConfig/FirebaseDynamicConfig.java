package com.abatra.android.wheelie.firebase.remoteConfig;

import static com.abatra.android.wheelie.firebase.GmsTaskUtils.log;
import static com.abatra.android.wheelie.firebase.GmsTaskUtils.logOnCompleteListener;

import android.app.Application;

import com.abatra.android.wheelie.core.remoteConfig.DynamicConfig;
import com.abatra.android.wheelie.core.remoteConfig.DynamicConfigSettings;
import com.google.firebase.FirebaseApp;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigValue;

import java.util.Optional;

import timber.log.Timber;

public class FirebaseDynamicConfig implements DynamicConfig {

    private FirebaseRemoteConfig firebaseRemoteConfig;

    @Override
    public void initialize(Application application) {
        FirebaseApp.initializeApp(application);
        firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
    }

    @Override
    public void initialize(DynamicConfigSettings settings) {
        firebaseRemoteConfig.ensureInitialized().addOnCompleteListener(task -> {
            log(task, "ensureInitialized");
            if (task.isSuccessful()) {
                FirebaseDynamicConfigSettings configSettings = (FirebaseDynamicConfigSettings) settings;

                firebaseRemoteConfig.setDefaultsAsync(configSettings.getDefaultValues())
                        .addOnCompleteListener(logOnCompleteListener("setDefaultsAsync"));

                firebaseRemoteConfig.setConfigSettingsAsync(createFirebaseRemoteConfigSettings(configSettings))
                        .addOnCompleteListener(logOnCompleteListener("setConfigSettingsAsync"));
            }
        });
    }


    private FirebaseRemoteConfigSettings createFirebaseRemoteConfigSettings(FirebaseDynamicConfigSettings settings) {
        return new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(settings.getMinFetchIntervalSeconds())
                .build();
    }

    @Override
    public String getStringValue(String key) {
        String string = getFirebaseRemoteConfigValue(key).asString();
        Timber.d("key=%s value=%s", key, string);
        return string;
    }

    private FirebaseRemoteConfigValue getFirebaseRemoteConfigValue(String key) {
        return getFirebaseRemoteConfig().getValue(key);
    }

    public FirebaseRemoteConfig getFirebaseRemoteConfig() {
        return Optional.ofNullable(firebaseRemoteConfig)
                .orElseThrow(() -> new IllegalStateException("Initialize this class using application initializer."));
    }

    @Override
    public long getLongValue(String key) {
        return getFirebaseRemoteConfigValue(key).asLong();
    }

    @Override
    public void fetchAndActivate() {
        getFirebaseRemoteConfig().fetchAndActivate().addOnCompleteListener(task -> log(task, "fetchAndActivate"));
    }
}
