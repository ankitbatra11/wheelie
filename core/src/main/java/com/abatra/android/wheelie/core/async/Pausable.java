package com.abatra.android.wheelie.core.async;

public interface Pausable {

    static Pausable atomicBoolean() {
        return new AtomicBooleanPausable();
    }

    void pause();

    boolean isPaused();

    void resume();
}
