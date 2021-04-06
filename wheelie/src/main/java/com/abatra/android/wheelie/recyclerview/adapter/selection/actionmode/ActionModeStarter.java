package com.abatra.android.wheelie.recyclerview.adapter.selection.actionmode;

import android.view.ActionMode;

import androidx.appcompat.widget.Toolbar;

public interface ActionModeStarter {

    static ActionModeStarter toolbar(Toolbar toolbar) {
        return toolbar::startActionMode;
    }

    ActionMode startActionMode(ActionMode.Callback callback);
}
