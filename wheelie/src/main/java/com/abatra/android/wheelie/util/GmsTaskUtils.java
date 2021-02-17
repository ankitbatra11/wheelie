package com.abatra.android.wheelie.util;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import timber.log.Timber;

public class GmsTaskUtils {

    private GmsTaskUtils() {
    }

    public static <T> OnCompleteListener<T> logOnCompleteListener(String operation) {
        return task -> logTask(task, operation);
    }

    public static void logTask(Task<?> task, String operation) {
        if (task.getException() != null) {
            Timber.e(task.getException(), "%s failed!", operation);
        } else {
            Timber.v(operation + " completed with result=" + task.getResult());
        }
    }
}
