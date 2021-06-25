package com.abatra.android.wheelie.yara.selection.actionmode;

import android.view.ActionMode;
import android.widget.Toolbar;

public interface ActionModeStarter {

    static ActionModeStarter toolbar(Toolbar toolbar) {
        return toolbar::startActionMode;
    }

    ActionMode startActionMode(ActionMode.Callback callback);
}
