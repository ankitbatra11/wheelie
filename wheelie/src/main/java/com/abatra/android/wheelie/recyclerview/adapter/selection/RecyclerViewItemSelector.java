package com.abatra.android.wheelie.recyclerview.adapter.selection;

import com.abatra.android.wheelie.lifecycle.LifecycleObserverObservable;

public interface RecyclerViewItemSelector extends LifecycleObserverObservable<RecyclerViewItemSelector.Listener>,
        SelectableRecyclerViewItem.Api {

    interface Listener {
        void onUpdate(SelectionState selectionState);
    }
}
