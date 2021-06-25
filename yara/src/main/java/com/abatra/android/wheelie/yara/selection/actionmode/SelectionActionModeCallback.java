package com.abatra.android.wheelie.yara.selection.actionmode;

import com.abatra.android.wheelie.yara.selection.RecyclerViewItemSelector;
import com.abatra.android.wheelie.yara.selection.SelectionState;

public interface SelectionActionModeCallback extends ActionModeCallback, RecyclerViewItemSelector.Listener {
    @Override
    default void onUpdate(SelectionState selectionState) {
    }
}
