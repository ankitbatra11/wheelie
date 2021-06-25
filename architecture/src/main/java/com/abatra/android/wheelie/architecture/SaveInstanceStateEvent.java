package com.abatra.android.wheelie.architecture;

import android.os.Bundle;

public class SaveInstanceStateEvent extends Event {

    private final Bundle state;

    public SaveInstanceStateEvent(Bundle state) {
        super(Event.TYPE_SAVE_INSTANCE_STATE);
        this.state = state;
    }

    public Bundle getState() {
        return state;
    }
}
