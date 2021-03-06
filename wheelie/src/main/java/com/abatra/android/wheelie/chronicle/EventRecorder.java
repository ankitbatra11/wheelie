package com.abatra.android.wheelie.chronicle;

import android.content.Context;

public interface EventRecorder {

    void record(Event event);

    interface Supplier {
        EventRecorder getEventRecorder(Context context);
    }
}
