package com.abatra.android.wheelie.firebase;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.startup.Initializer;

import com.google.firebase.FirebaseApp;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class FirebaseAppInitializer implements Initializer<Void> {

    @NonNull
    @NotNull
    @Override
    public Void create(@NonNull @NotNull Context context) {
        FirebaseApp.initializeApp(context);
        return null;
    }

    @NonNull
    @NotNull
    @Override
    public List<Class<? extends Initializer<?>>> dependencies() {
        return Collections.emptyList();
    }
}
