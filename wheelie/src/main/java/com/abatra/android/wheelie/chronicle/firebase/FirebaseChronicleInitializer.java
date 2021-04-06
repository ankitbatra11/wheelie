package com.abatra.android.wheelie.chronicle.firebase;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.startup.Initializer;

import com.abatra.android.wheelie.chronicle.Chronicle;

import java.util.Collections;
import java.util.List;

public class FirebaseChronicleInitializer implements Initializer<Void> {

    @NonNull
    @Override
    public Void create(@NonNull Context context) {
        Chronicle.initializeForFirebase(context);
        return null;
    }

    @NonNull
    @Override
    public List<Class<? extends Initializer<?>>> dependencies() {
        return Collections.emptyList();
    }
}
