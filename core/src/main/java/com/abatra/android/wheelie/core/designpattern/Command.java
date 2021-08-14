package com.abatra.android.wheelie.core.designpattern;

public interface Command {

    default void execute() {
    }

    default void undo() {
    }
}
