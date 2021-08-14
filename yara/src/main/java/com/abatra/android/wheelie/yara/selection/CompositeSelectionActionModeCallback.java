package com.abatra.android.wheelie.yara.selection;

import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;

import com.abatra.android.wheelie.core.designpattern.Observable;
import com.abatra.android.wheelie.yara.selection.actionmode.SelectionActionModeCallback;

import java.util.function.Consumer;

public class CompositeSelectionActionModeCallback implements SelectionActionModeCallback,
        Observable<SelectionActionModeCallback> {

    private final CompositeActionModeCallback compositeActionModeCallback = new CompositeActionModeCallback();
    private final Observable<RecyclerViewItemSelector.Listener> listeners = Observable.hashSet();

    @Override
    public void addObserver(SelectionActionModeCallback observer) {
        compositeActionModeCallback.addObserver(observer);
        listeners.addObserver(observer);
    }

    @Override
    public void removeObserver(SelectionActionModeCallback observer) {
        compositeActionModeCallback.removeObserver(observer);
        listeners.removeObserver(observer);
    }

    @Override
    public void onUpdate(SelectionState selectionState) {
        listeners.forEachObserver(listener -> listener.onUpdate(selectionState));
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        return compositeActionModeCallback.onCreateActionMode(mode, menu);
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return compositeActionModeCallback.onPrepareActionMode(mode, menu);
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        return compositeActionModeCallback.onActionItemClicked(mode, item);
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        compositeActionModeCallback.onDestroyActionMode(mode);
    }

    public void forEachSelectionListener(Consumer<RecyclerViewItemSelector.Listener> listenerConsumer) {
        listeners.forEachObserver(listenerConsumer);
    }

}
