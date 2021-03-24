package com.abatra.android.wheelie.chronicle;

import android.content.Context;

import com.abatra.android.wheelie.chronicle.firebase.FirebaseEventBuilder;
import com.abatra.android.wheelie.chronicle.firebase.FirebaseEventRecorder;
import com.abatra.android.wheelie.chronicle.firebase.FirebaseUserPropertySetter;
import com.google.firebase.analytics.FirebaseAnalytics;

class ChronicleConfig {

    public static final FirebaseEventBuilder.Factory FACTORY_FIREBASE_EVENT_BUILDER = new FirebaseEventBuilder.Factory();

    private final EventBuilder.Factory factory;
    private final EventRecorder eventRecorder;
    private final UserPropertySetter userPropertySetter;

    ChronicleConfig(EventBuilder.Factory factory,
                    EventRecorder eventRecorder,
                    UserPropertySetter userPropertySetter) {
        this.factory = factory;
        this.eventRecorder = eventRecorder;
        this.userPropertySetter = userPropertySetter;
    }

    static ChronicleConfig forFirebase(Context context) {
        FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(context);
        return new ChronicleConfig(FACTORY_FIREBASE_EVENT_BUILDER,
                new FirebaseEventRecorder(firebaseAnalytics),
                new FirebaseUserPropertySetter(firebaseAnalytics));
    }

    EventBuilder.Factory getEventBuilderFactory() {
        return factory;
    }

    EventRecorder getEventRecorder() {
        return eventRecorder;
    }

    UserPropertySetter getUserPropertySetter() {
        return userPropertySetter;
    }
}
