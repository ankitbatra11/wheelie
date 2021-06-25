package com.abatra.android.wheelie.logger;

import android.content.Context;

import androidx.startup.Initializer;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

import timber.log.Timber;

abstract public class TimberInitializer implements Initializer<Void> {

    public static final Timber.DebugTree DEBUG_TREE = new Timber.DebugTree();

    @NotNull
    @Override
    public Void create(@NotNull Context context) {
        Timber.plant(isDebug() ? getDebugTree() : getReleaseTree());
        return null;
    }

    protected abstract boolean isDebug();

    protected Timber.Tree getDebugTree() {
        return DEBUG_TREE;
    }

    protected abstract Timber.Tree getReleaseTree();

    @NotNull
    @Override
    public List<Class<? extends Initializer<?>>> dependencies() {
        return Collections.emptyList();
    }
}
