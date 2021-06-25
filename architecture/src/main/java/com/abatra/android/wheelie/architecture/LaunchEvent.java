package com.abatra.android.wheelie.architecture;

import android.content.Intent;

import java.util.Optional;

public class LaunchEvent extends Event {

    private final Intent intent;

    public LaunchEvent(Intent intent) {
        super(Event.TYPE_LAUNCH);
        this.intent = intent;
    }

    public Optional<Intent> getIntent() {
        return Optional.ofNullable(intent);
    }
}
