package com.abatra.android.wheelie.core.notification;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

public class NotificationBuilder extends NotificationCompat.Builder {

    private final Context context;

    public NotificationBuilder(@NonNull Context context, @NonNull String channelId) {
        super(context, channelId);
        this.context = context;
    }

    public void headsUpNotification() {
        setPriority(NotificationCompat.PRIORITY_HIGH);
        setVibrate(new long[0]);
    }

    public void setContentActivityIntent(Intent intent) {
        setContentActivityIntent(intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public void setContentActivityIntent(Intent intent, int pendingIntentFlag) {
        setContentIntent(PendingIntent.getActivity(context, 0, intent, pendingIntentFlag));
    }
}
