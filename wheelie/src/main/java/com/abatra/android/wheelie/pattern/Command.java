package com.abatra.android.wheelie.pattern;

public interface Command {

    default void execute() {
    }

    default void undo() {
    }
}
