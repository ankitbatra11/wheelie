package com.abatra.android.wheelie.recyclerview.adapter.selection.actionmode;

import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;

public interface ActionModeCallback extends ActionMode.Callback {

    @Override
    default boolean onCreateActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    default boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    default boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        return false;
    }

    @Override
    default void onDestroyActionMode(ActionMode mode) {

    }
}
