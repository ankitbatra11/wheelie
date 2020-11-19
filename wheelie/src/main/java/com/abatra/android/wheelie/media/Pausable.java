package com.abatra.android.wheelie.media;

public interface Pausable {

    static Pausable atomicBoolean() {
        return new AtomicBooleanPausable();
    }

    void pause();

    boolean isPaused();

    void resume();
}
