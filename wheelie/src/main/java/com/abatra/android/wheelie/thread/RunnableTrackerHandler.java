package com.abatra.android.wheelie.thread;

import android.os.Handler;

import java.util.Collection;
import java.util.HashSet;

import timber.log.Timber;

public class RunnableTrackerHandler {

    private final Handler handler;
    private final Collection<Runnable> runnables = new HashSet<>();

    private RunnableTrackerHandler(Handler handler) {
        this.handler = handler;
    }

    public static RunnableTrackerHandler wrap(Handler handler) {
        return new RunnableTrackerHandler(handler);
    }

    public void post(Runnable runnable) {
        runnables.add(runnable);
        handler.post(runnable);
    }

    public void postDelayed(Runnable runnable, long delayMillis) {
        runnables.add(runnable);
        handler.postDelayed(runnable, delayMillis);
    }

    public void removeAllCallbacks() {
        for (Runnable runnable : runnables) {
            try {
                handler.removeCallbacks(runnable);
            } catch (Throwable t) {
                Timber.e(t);
            }
        }
    }
}
