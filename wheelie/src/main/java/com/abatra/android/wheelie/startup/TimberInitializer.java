package com.abatra.android.wheelie.startup;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.startup.Initializer;

import com.google.firebase.crashlytics.FirebaseCrashlytics;

import java.util.Collections;
import java.util.List;

import timber.log.Timber;

abstract public class TimberInitializer implements Initializer<Void> {

    public static final Timber.DebugTree DEBUG_TREE = new Timber.DebugTree();

    @NonNull
    @Override
    public Void create(@NonNull Context context) {
        Timber.plant(isDebug() ? getDebugTree() : getReleaseTree());
        return null;
    }

    protected abstract boolean isDebug();

    protected Timber.Tree getDebugTree() {
        return DEBUG_TREE;
    }

    protected Timber.Tree getReleaseTree() {
        return new CrashlyticsTimberTree(FirebaseCrashlytics.getInstance());
    }

    @NonNull
    @Override
    public List<Class<? extends Initializer<?>>> dependencies() {
        return Collections.emptyList();
    }
}
