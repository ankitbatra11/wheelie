package com.abatra.android.wheelie.recyclerview.adapter.selection.actionmode;

import android.view.ActionMode;

import com.abatra.android.wheelie.pattern.Observable;
import com.abatra.android.wheelie.recyclerview.adapter.selection.CompositeActionModeCallback;
import com.abatra.android.wheelie.recyclerview.adapter.selection.CompositeSelectionActionModeCallback;
import com.abatra.android.wheelie.recyclerview.adapter.selection.RecyclerViewItemSelector;
import com.abatra.android.wheelie.recyclerview.adapter.selection.SelectionRecyclerViewAdapter;
import com.abatra.android.wheelie.recyclerview.adapter.selection.SelectionState;

import java.util.Optional;

import javax.annotation.Nullable;

import timber.log.Timber;

public class ActionModeSelectionStateSyncer implements RecyclerViewItemSelector.Listener, SelectionActionModeCallback,
        Observable<SelectionActionModeCallback> {

    private final SelectionRecyclerViewAdapter<?> recyclerViewAdapter;
    private final ActionModeStarter actionModeStarter;
    private final CompositeSelectionActionModeCallback callbacks = new CompositeSelectionActionModeCallback();
    @Nullable
    private ActionMode actionMode;

    public ActionModeSelectionStateSyncer(SelectionRecyclerViewAdapter<?> recyclerViewAdapter,
                                          ActionModeStarter actionModeStarter) {
        this.recyclerViewAdapter = recyclerViewAdapter;
        this.actionModeStarter = actionModeStarter;
    }

    @Override
    public void addObserver(SelectionActionModeCallback observer) {
        callbacks.addObserver(observer);
    }

    @Override
    public void removeObserver(SelectionActionModeCallback observer) {
        callbacks.addObserver(observer);
    }

    @Override
    public void onUpdate(SelectionState selectionState) {
        if (inActionMode()) {
            if (!selectionState.hasSelection()) {
                finishActionMode();
            } else {
                Timber.d("action mode is already started");
            }
        } else {
            if (selectionState.hasSelection()) {
                startActionMode();
            } else {
                Timber.d("no items are selected to start action mode");
            }
        }
        getActionMode().ifPresent(actionMode -> actionMode.setTitle(String.valueOf(selectionState.getSelectedItemCount())));
        callbacks.forEachSelectionListener(callback -> callback.onUpdate(selectionState));
    }

    private void startActionMode() {
        CompositeActionModeCallback actionModeCallback = new CompositeActionModeCallback(callbacks, this);
        actionMode = actionModeStarter.startActionMode(actionModeCallback);
    }

    private void finishActionMode() {
        getActionMode().ifPresent(ActionMode::finish);
    }

    private boolean inActionMode() {
        return getActionMode().isPresent();
    }

    private Optional<ActionMode> getActionMode() {
        return Optional.ofNullable(actionMode);
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        recyclerViewAdapter.clearSelection(); // it should trigger action mode finish
        actionMode = null;
    }
}
