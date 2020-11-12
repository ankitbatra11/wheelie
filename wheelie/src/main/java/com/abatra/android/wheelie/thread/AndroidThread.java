package com.abatra.android.wheelie.thread;

import java.util.concurrent.TimeUnit;

public interface AndroidThread {

    void startAndroidThread();

    void postRunnableWithScheduledInterval(Runnable runnable, long intervalDuration, TimeUnit intervalUnit);

    void stopAndroidThread();
}
