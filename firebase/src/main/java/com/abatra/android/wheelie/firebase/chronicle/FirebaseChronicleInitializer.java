package com.abatra.android.wheelie.firebase.chronicle;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.startup.Initializer;

import com.abatra.android.wheelie.chronicle.Chronicle;
import com.abatra.android.wheelie.chronicle.ChronicleConfig;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.Collections;
import java.util.List;

public class FirebaseChronicleInitializer implements Initializer<Void> {

    @NonNull
    @Override
    public Void create(@NonNull Context context) {
        FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(context);
        Chronicle.setConfig(new ChronicleConfig(
                FirebaseEventBuilder.Factory.getInstance(),
                new FirebaseEventRecorder(firebaseAnalytics),
                new FirebaseUserPropertySetter(firebaseAnalytics)
        ));
        return null;
    }

    @NonNull
    @Override
    public List<Class<? extends Initializer<?>>> dependencies() {
        return Collections.emptyList();
    }
}
