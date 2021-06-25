package com.abatra.android.wheelie.designPattern;

public interface Command {

    default void execute() {
    }

    default void undo() {
    }
}
