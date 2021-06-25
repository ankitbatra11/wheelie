package com.abatra.android.wheelie.architecture;

import androidx.annotation.NonNull;

public class Event {

    public static final int TYPE_LOAD_VIEW_STATE = 0;
    public static final int TYPE_LAUNCH = 1;
    public static final int TYPE_SAVE_INSTANCE_STATE = 2;

    private final int type;

    public Event(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    @NonNull
    @Override
    public String toString() {
        return "Event{" +
                "type=" + type +
                '}';
    }
}
