package com.abatra.android.wheelie.firebase.chronicle;

import com.abatra.android.wheelie.chronicle.Event;
import com.abatra.android.wheelie.chronicle.EventRecorder;
import com.google.firebase.analytics.FirebaseAnalytics;

public class FirebaseEventRecorder implements EventRecorder {

    private final FirebaseAnalytics firebaseAnalytics;

    public FirebaseEventRecorder(FirebaseAnalytics firebaseAnalytics) {
        this.firebaseAnalytics = firebaseAnalytics;
    }

    @Override
    public void record(Event event) {
        firebaseAnalytics.logEvent(event.getName(), event.getEventParams().bundle());
    }
}
