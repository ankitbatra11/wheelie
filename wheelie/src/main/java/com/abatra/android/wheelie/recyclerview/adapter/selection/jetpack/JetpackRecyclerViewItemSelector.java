package com.abatra.android.wheelie.recyclerview.adapter.selection.jetpack;

import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.selection.StorageStrategy;
import androidx.recyclerview.widget.RecyclerView;

import com.abatra.android.wheelie.lifecycle.owner.ILifecycleOwner;
import com.abatra.android.wheelie.recyclerview.adapter.RecyclerViewItem;
import com.abatra.android.wheelie.recyclerview.adapter.selection.AbstractRecyclerViewItemSelector;
import com.abatra.android.wheelie.recyclerview.adapter.selection.SelectableRecyclerViewItem;
import com.abatra.android.wheelie.recyclerview.adapter.selection.SelectionRecyclerViewAdapter;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import timber.log.Timber;

public class JetpackRecyclerViewItemSelector<K> extends AbstractRecyclerViewItemSelector
        implements SelectableRecyclerViewItem.Api {

    private static final String DEFAULT_SELECTION_ID = "JetpackRecyclerViewItemSelector";

    private final SelectionRecyclerViewAdapter<?> recyclerViewAdapter;
    private final SelectionTracker<K> selectionTracker;

    JetpackRecyclerViewItemSelector(SelectionRecyclerViewAdapter<?> recyclerViewAdapter, SelectionTracker<K> selectionTracker) {
        this.recyclerViewAdapter = recyclerViewAdapter;
        this.selectionTracker = selectionTracker;
        this.selectionTracker.addObserver(new SelectionListener());
    }

    public static <K> JetpackRecyclerViewItemSelector<K> createItemSelector(SelectionRecyclerViewAdapter<?> recyclerViewAdapter,
                                                                            RecyclerView recyclerView,
                                                                            StorageStrategy<K> storageStrategy) {

        JetpackItemKeyProvider<K> itemKeyProvider = JetpackItemKeyProvider.cached(recyclerViewAdapter);

        SelectionTracker.Builder<K> builder = new SelectionTracker.Builder<>(DEFAULT_SELECTION_ID,
                recyclerView,
                itemKeyProvider,
                new JetpackSelectableItemDetailsLookup<>(recyclerView, itemKeyProvider),
                storageStrategy);

        return new JetpackRecyclerViewItemSelector<>(recyclerViewAdapter, builder.build());
    }

    @Override
    public void observeLifecycle(ILifecycleOwner lifecycleOwner) {
        lifecycleOwner.getLifecycle().addObserver(this);
    }

    @Override
    public int getPositionOfKey(Object key) {
        return recyclerViewAdapter.getPositionOfItem(recyclerViewItem -> {
            if (recyclerViewItem instanceof SelectableRecyclerViewItem) {
                SelectableRecyclerViewItem viewItem = (SelectableRecyclerViewItem) recyclerViewItem;
                return key.equals(viewItem.getKey());
            }
            return false;
        });
    }

    @Override
    public void clearSelection() {
        selectionTracker.clearSelection();
    }

    @Override
    public boolean isSelected(SelectableRecyclerViewItem selectableRecyclerViewItem) {
        //noinspection unchecked
        return selectionTracker.getSelection().contains((K) selectableRecyclerViewItem.getKey());
    }

    @Override
    public int getSelectedItemCount() {
        return selectionTracker.getSelection().size();
    }

    @Override
    public List<RecyclerViewItem> getSelectedItems() {
        return recyclerViewAdapter.getItems(isSelectedPredicate());
    }

    private Predicate<RecyclerViewItem> isSelectedPredicate() {
        return recyclerViewItem -> selectionTracker.getSelection().contains(getKey(recyclerViewItem));
    }

    private K getKey(RecyclerViewItem recyclerViewItem) {
        SelectableRecyclerViewItem item = (SelectableRecyclerViewItem) recyclerViewItem;
        //noinspection unchecked
        return (K) item.getKey();
    }

    @Override
    public void selectAll() {
        List<K> unselectedKeys = getUnselectedKeys();
        if (!unselectedKeys.isEmpty()) {
            selectionTracker.setItemsSelected(unselectedKeys, true);
        }
    }

    private boolean isSelected(Object key) {
        //noinspection unchecked
        return selectionTracker.getSelection().contains((K) key);
    }

    private List<K> getUnselectedKeys() {
        //noinspection unchecked
        return (List<K>) getNonNullKeyStream()
                .filter(k -> !isSelected(k))
                .collect(Collectors.toList());
    }

    private Stream<Object> getNonNullKeyStream() {
        return recyclerViewAdapter.getItems().stream()
                .map(item -> (SelectableRecyclerViewItem) item)
                .map(SelectableRecyclerViewItem::getKey)
                .filter(Objects::nonNull);
    }

    @Override
    public List<?> getSelectedItemKeys() {
        return getNonNullKeyStream()
                .filter(this::isSelected)
                .collect(Collectors.toList());
    }

    @Override
    public boolean hasSelection() {
        return selectionTracker.hasSelection();
    }

    @Override
    public void onDestroy() {
        selectionTracker.clearSelection();
    }

    /**
     * TODO: Override rest of the methods as needed.
     */
    private class SelectionListener extends SelectionTracker.SelectionObserver<K> {
        @Override
        public void onSelectionChanged() {
            super.onSelectionChanged();
            Timber.d("onSelectionChanged tracker=%s", selectionTracker);
            forEachObserver(type -> type.onUpdate(new JetpackSelectionState(selectionTracker.getSelection().size())));
        }
    }
}
