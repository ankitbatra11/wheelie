package com.abatra.android.wheelie.notification;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

public class NotificationBuilder extends NotificationCompat.Builder {

    public NotificationBuilder(@NonNull Context context, @NonNull String channelId) {
        super(context, channelId);
    }

    public NotificationBuilder headsUpNotification() {
        setPriority(NotificationCompat.PRIORITY_HIGH);
        setVibrate(new long[0]);
        return this;
    }
}
