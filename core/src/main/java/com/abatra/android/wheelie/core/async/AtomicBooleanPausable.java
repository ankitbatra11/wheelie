package com.abatra.android.wheelie.core.async;

import java.util.concurrent.atomic.AtomicBoolean;

public class AtomicBooleanPausable implements Pausable {

    private final AtomicBoolean paused = new AtomicBoolean(false);

    AtomicBooleanPausable() {
    }

    @Override
    public void pause() {
        paused.set(true);
    }

    @Override
    public boolean isPaused() {
        return paused.get();
    }

    @Override
    public void resume() {
        paused.set(false);
    }
}
