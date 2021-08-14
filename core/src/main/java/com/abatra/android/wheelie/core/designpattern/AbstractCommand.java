package com.abatra.android.wheelie.core.designpattern;

import timber.log.Timber;

abstract public class AbstractCommand implements Command {

    @Override
    public void execute() {
        try {
            executeOrThrow();
        } catch (Throwable error) {
            Timber.e(error);
        }
    }

    protected abstract void executeOrThrow();
}
