package com.abatra.android.wheelie.chameleon.firebase;

import com.abatra.android.wheelie.chameleon.DynamicConfig;
import com.abatra.android.wheelie.chameleon.DynamicConfigSettings;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigValue;

import timber.log.Timber;

import static com.abatra.android.wheelie.util.GmsTaskUtils.logOnCompleteListener;
import static com.abatra.android.wheelie.util.GmsTaskUtils.logTask;

public class FirebaseDynamicConfig implements DynamicConfig {

    private final FirebaseRemoteConfig firebaseRemoteConfig;

    public FirebaseDynamicConfig(FirebaseRemoteConfig firebaseRemoteConfig) {
        this.firebaseRemoteConfig = firebaseRemoteConfig;
    }

    @Override
    public void initialize(DynamicConfigSettings settings) {

        firebaseRemoteConfig.ensureInitialized().addOnCompleteListener(task -> {

            logTask(task, "ensureInitialized");

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
        return firebaseRemoteConfig.getValue(key);
    }

    @Override
    public long getLongValue(String key) {
        return getFirebaseRemoteConfigValue(key).asLong();
    }

    @Override
    public void fetchAndActivate() {
        firebaseRemoteConfig.fetchAndActivate().addOnCompleteListener(task -> logTask(task, "fetchAndActivate"));
    }
}
