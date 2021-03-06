package com.abatra.android.wheelie.chronicle;

import android.os.Bundle;
import android.os.Parcelable;

public class BundleEventParams implements EventParams {

    private final Bundle bundle;

    BundleEventParams(Bundle bundle) {
        this.bundle = bundle;
    }

    @Override
    public void put(String key, String value) {
        bundle.putString(key, value);
    }

    @Override
    public Bundle bundle() {
        return bundle;
    }

    @Override
    public void put(String key, double value) {
        bundle.putDouble(key, value);
    }

    @Override
    public void put(String key, long value) {
        bundle.putLong(key, value);
    }

    @Override
    public void put(String key, Parcelable[] value) {
        bundle.putParcelableArray(key, value);
    }

    @Override
    public void put(String key, int value) {
        bundle.putInt(key, value);
    }

    @Override
    public void put(String key, boolean value) {
        bundle.putBoolean(key, value);
    }
}
