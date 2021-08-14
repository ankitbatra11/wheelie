package com.abatra.android.wheelie.yara.selection;

import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;

import com.abatra.android.wheelie.core.designpattern.CollectionObservable;
import com.abatra.android.wheelie.core.designpattern.Observable;
import com.google.common.collect.Sets;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

public class CompositeActionModeCallback implements ActionMode.Callback, Observable<ActionMode.Callback> {

    private final Observable<ActionMode.Callback> callbacks;
    private final ActionModeCompositeResultCalculator onCreateResultCalculator = new ActionModeCompositeResultCalculator() {
        @Override
        boolean call(ActionMode.Callback callback, ActionMode actionMode, Menu menu, MenuItem menuItem) {
            return callback.onCreateActionMode(actionMode, menu);
        }
    };
    private final ActionModeCompositeResultCalculator onPrepareResultCalculator = new ActionModeCompositeResultCalculator() {
        @Override
        boolean call(ActionMode.Callback callback, ActionMode actionMode, Menu menu, MenuItem menuItem) {
            return callback.onPrepareActionMode(actionMode, menu);
        }
    };
    private final ActionModeCompositeResultCalculator onActionItemClickedResultCalculator = new ActionModeCompositeResultCalculator() {
        @Override
        boolean call(ActionMode.Callback callback, ActionMode actionMode, Menu menu, MenuItem menuItem) {
            return callback.onActionItemClicked(actionMode, menuItem);
        }
    };

    public CompositeActionModeCallback(ActionMode.Callback... callbacks) {
        this.callbacks = new CollectionObservable<>(Sets.newLinkedHashSet(Arrays.asList(callbacks)));
    }

    public CompositeActionModeCallback() {
        this.callbacks = Observable.hashSet();
    }

    @Override
    public void addObserver(ActionMode.Callback observer) {
        callbacks.addObserver(observer);
    }

    @Override
    public void removeObserver(ActionMode.Callback observer) {
        callbacks.removeObserver(observer);
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        return onCreateResultCalculator.calculateResult(mode, menu, null);
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return onPrepareResultCalculator.calculateResult(mode, menu, null);
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        return onActionItemClickedResultCalculator.calculateResult(mode, null, item);
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        callbacks.forEachObserver(callback -> callback.onDestroyActionMode(mode));
    }

    private abstract class ActionModeCompositeResultCalculator {

        private boolean calculateResult(ActionMode actionMode, Menu menu, MenuItem menuItem) {
            final AtomicBoolean result = new AtomicBoolean(false);
            callbacks.forEachObserver(callback -> result.set(result.get() | call(callback, actionMode, menu, menuItem)));
            return result.get();
        }

        abstract boolean call(ActionMode.Callback callback, ActionMode actionMode, Menu menu, MenuItem menuItem);

    }
}
