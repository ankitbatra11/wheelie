package com.abatra.android.wheelie.chameleon.firebase;

import androidx.annotation.Nullable;

import com.abatra.android.wheelie.chameleon.DynamicConfigSettings;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class FirebaseDynamicConfigSettings implements DynamicConfigSettings {

    private final long minFetchIntervalSeconds;
    private final Map<String, Object> defaultValues;

    public FirebaseDynamicConfigSettings(long minFetchIntervalSeconds, Map<String, Object> defaultValues) {
        this.minFetchIntervalSeconds = minFetchIntervalSeconds;
        this.defaultValues = defaultValues;
    }

    public long getMinFetchIntervalSeconds() {
        return minFetchIntervalSeconds;
    }

    public Map<String, Object> getDefaultValues() {
        return defaultValues;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private Long minFetchIntervalSeconds;
        private final Map<String, Object> defaultValues = new HashMap<>();

        private Builder() {
        }

        public Builder setMinFetchIntervalSeconds(@Nullable Long minFetchIntervalSeconds) {
            this.minFetchIntervalSeconds = minFetchIntervalSeconds;
            return this;
        }

        public Builder debug() {
            return setMinFetchIntervalSeconds(5L);
        }

        public Builder addDefaultValue(String key, Object value) {
            defaultValues.put(key, value);
            return this;
        }

        public FirebaseDynamicConfigSettings build() {
            return new FirebaseDynamicConfigSettings(getMinFetchIntervalSeconds(), defaultValues);
        }

        private long getMinFetchIntervalSeconds() {
            return minFetchIntervalSeconds == null
                    ? TimeUnit.HOURS.toSeconds(1)
                    : minFetchIntervalSeconds;
        }

    }

}
