package com.abatra.android.wheelie.recyclerview.adapter.selection.jetpack;

import com.abatra.android.wheelie.recyclerview.adapter.selection.SelectionState;

class JetpackSelectionState implements SelectionState {

    private final int selectionItemCount;

    JetpackSelectionState(int selectionItemCount) {
        this.selectionItemCount = selectionItemCount;
    }

    @Override
    public boolean hasSelection() {
        return selectionItemCount > 0;
    }

    @Override
    public int getSelectedItemCount() {
        return selectionItemCount;
    }
}
