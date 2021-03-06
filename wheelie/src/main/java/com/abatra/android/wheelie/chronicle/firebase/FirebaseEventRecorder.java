package com.abatra.android.wheelie.chronicle.firebase;

import android.content.Context;

import com.abatra.android.wheelie.chronicle.Event;
import com.abatra.android.wheelie.chronicle.EventRecorder;
import com.google.firebase.analytics.FirebaseAnalytics;

public class FirebaseEventRecorder implements EventRecorder {

    private static FirebaseEventRecorder gInstance;

    private final FirebaseAnalytics firebaseAnalytics;

    private FirebaseEventRecorder(FirebaseAnalytics firebaseAnalytics) {
        this.firebaseAnalytics = firebaseAnalytics;
    }

    static FirebaseEventRecorder getInstance(Context context) {
        if (gInstance == null) {
            synchronized (FirebaseEventRecorder.class) {
                if (gInstance == null) {
                    gInstance = new FirebaseEventRecorder(FirebaseAnalytics.getInstance(context.getApplicationContext()));
                }
            }
        }
        return gInstance;
    }

    @Override
    public void record(Event event) {
        firebaseAnalytics.logEvent(event.getName(), event.getEventParams().bundle());
    }

    public static class Supplier implements EventRecorder.Supplier {

        public static final Supplier INSTANCE = new Supplier();

        private Supplier() {
        }

        @Override
        public EventRecorder getEventRecorder(Context context) {
            return FirebaseEventRecorder.getInstance(context);
        }
    }
}
