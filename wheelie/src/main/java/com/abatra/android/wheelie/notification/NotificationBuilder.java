package com.abatra.android.wheelie.notification;

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

    public NotificationBuilder headsUpNotification() {
        setPriority(NotificationCompat.PRIORITY_HIGH);
        setVibrate(new long[0]);
        return this;
    }

    public NotificationBuilder setContentActivityIntent(Intent intent) {
        setContentIntent(PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT));
        return this;
    }
}
