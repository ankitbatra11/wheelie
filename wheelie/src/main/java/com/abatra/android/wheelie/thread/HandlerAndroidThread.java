package com.abatra.android.wheelie.thread;

import android.os.Handler;
import android.os.HandlerThread;

import androidx.annotation.Nullable;

import java.util.concurrent.TimeUnit;

import timber.log.Timber;

public class HandlerAndroidThread implements AndroidThread {

    private final String name;

    @Nullable
    private HandlerThread handlerThread;

    @Nullable
    private RunnableTrackerHandler handler;

    public HandlerAndroidThread(String name) {
        this.name = name;
    }

    @Override
    public void startAndroidThread() {
        handlerThread = new HandlerThread(name);
        handlerThread.start();
        handler = RunnableTrackerHandler.wrap(new Handler(handlerThread.getLooper()));
    }

    @Override
    public void postRunnableWithScheduledInterval(Runnable runnable, long intervalDuration, TimeUnit intervalUnit) {
        if (handler == null) {
            throw new IllegalStateException("Must call start android thread before posting a message");
        }
        Runnable decoratedRunnable = new Runnable() {
            @Override
            public void run() {
                try {
                    runnable.run();
                } catch (Throwable t) {
                    Timber.e(t);
                } finally {
                    if (handler != null) {
                        handler.postDelayed(this, intervalUnit.toMillis(intervalDuration));
                    } else {
                        Timber.d("Handler is null. Can't post next runnable.");
                    }
                }
            }
        };
        handler.post(decoratedRunnable);
    }

    @Override
    public void stopAndroidThread() {
        if (handler != null) {
            handler.removeAllCallbacks();
            handler = null;
        }
        if (handlerThread != null) {
            handlerThread.quit();
            handlerThread = null;
        }
    }
}
