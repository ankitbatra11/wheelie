package com.abatra.android.wheelie.yara.selection;

import com.abatra.android.wheelie.lifecycle.observer.LifecycleObserverObservable;

public interface RecyclerViewItemSelector extends LifecycleObserverObservable<RecyclerViewItemSelector.Listener>,
        SelectableRecyclerViewItem.Api {

    interface Listener {
        void onUpdate(SelectionState selectionState);
    }
}
