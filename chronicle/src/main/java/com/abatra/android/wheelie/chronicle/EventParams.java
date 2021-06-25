package com.abatra.android.wheelie.chronicle;

import android.os.Bundle;
import android.os.Parcelable;

public interface EventParams {

    static EventParams bundled() {
        return new BundleEventParams(new Bundle());
    }

    void put(String key, String value);

    void put(String key, double value);

    void put(String key, long value);

    void put(String key, Parcelable[] value);

    void put(String key, int value);

    void put(String key, boolean value);

    Bundle bundle();
}
