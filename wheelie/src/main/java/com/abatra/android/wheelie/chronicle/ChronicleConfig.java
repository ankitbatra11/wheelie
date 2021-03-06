package com.abatra.android.wheelie.chronicle;

import android.content.Context;

class ChronicleConfig {

    private final Context context;
    private final EventBuilder.Factory factory;
    private final EventRecorder.Supplier supplier;

    ChronicleConfig(Context context, EventBuilder.Factory factory, EventRecorder.Supplier supplier) {
        this.context = context.getApplicationContext();
        this.factory = factory;
        this.supplier = supplier;
    }

    Context getContext() {
        return context;
    }

    EventBuilder.Factory getEventBuilderFactory() {
        return factory;
    }

    EventRecorder.Supplier getEventRecorderSupplier() {
        return supplier;
    }
}
