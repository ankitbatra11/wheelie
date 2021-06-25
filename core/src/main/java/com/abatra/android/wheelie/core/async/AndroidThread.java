package com.abatra.android.wheelie.core.async;

import java.util.concurrent.TimeUnit;

public interface AndroidThread extends Pausable {

    void startAndroidThread();

    void postRunnable(Runnable runnable);

    void postRunnableWithDelay(Runnable runnable, long delayDuration, TimeUnit durationUnit);

    void postRunnableWithScheduledInterval(Runnable runnable, long intervalDuration, TimeUnit intervalUnit);

    void stopAndroidThread();
}
