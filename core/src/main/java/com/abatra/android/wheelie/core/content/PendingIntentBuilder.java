package com.abatra.android.wheelie.core.content;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.TaskStackBuilder;

public class PendingIntentBuilder {

    private final Context context;
    private final Intent intent;
    int pendingIntentFlags = PendingIntent.FLAG_UPDATE_CURRENT;

    public PendingIntentBuilder(Context context, Intent intent) {
        this.context = context;
        this.intent = intent;
    }

    public PendingIntentBuilder cancelCurrent() {
        pendingIntentFlags = PendingIntent.FLAG_CANCEL_CURRENT;
        return this;
    }

    public PendingIntent buildParentStackPendingIntent() {
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);
        taskStackBuilder.addNextIntentWithParentStack(intent);
        return taskStackBuilder.getPendingIntent(0, pendingIntentFlags);
    }

    /**
     * @return {@link PendingIntent} for activities without back stack.
     */
    public PendingIntent buildSpecialActivityPendingIntent() {
        return PendingIntent.getActivity(context,
                0,
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK),
                pendingIntentFlags);
    }
}
