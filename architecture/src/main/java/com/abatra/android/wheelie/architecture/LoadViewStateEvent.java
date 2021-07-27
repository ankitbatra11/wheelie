package com.abatra.android.wheelie.architecture;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.abatra.android.wheelie.core.content.Intents;

import java.util.Optional;

public class LoadViewStateEvent extends Event {

    private Bundle arguments;
    private Bundle savedState;
    private Intent launchIntent;

    public LoadViewStateEvent() {
        super(TYPE_LOAD_VIEW_STATE);
    }

    public LoadViewStateEvent setArguments(Bundle arguments) {
        this.arguments = arguments;
        return this;
    }

    public LoadViewStateEvent setSavedState(Bundle savedState) {
        this.savedState = savedState;
        return this;
    }

    public LoadViewStateEvent setLaunchIntent(Intent launchIntent) {
        this.launchIntent = launchIntent;
        return this;
    }

    public Optional<Bundle> getArguments() {
        return Optional.ofNullable(arguments);
    }

    public Optional<Bundle> getSavedState() {
        return Optional.ofNullable(savedState);
    }

    public Optional<Intent> getLaunchIntent() {
        return Optional.ofNullable(launchIntent);
    }

    @NonNull
    @Override
    public String toString() {
        return "LoadViewStateEvent{" +
                "arguments=" + arguments +
                ", savedState=" + savedState +
                ", launchIntent=" + Intents.print(launchIntent) +
                "} " + super.toString();
    }
}
