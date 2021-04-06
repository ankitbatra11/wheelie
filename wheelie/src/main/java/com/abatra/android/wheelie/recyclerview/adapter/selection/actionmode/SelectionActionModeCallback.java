package com.abatra.android.wheelie.recyclerview.adapter.selection.actionmode;

import com.abatra.android.wheelie.recyclerview.adapter.selection.RecyclerViewItemSelector;
import com.abatra.android.wheelie.recyclerview.adapter.selection.SelectionState;

public interface SelectionActionModeCallback extends ActionModeCallback, RecyclerViewItemSelector.Listener {
    @Override
    default void onUpdate(SelectionState selectionState) {
    }
}
