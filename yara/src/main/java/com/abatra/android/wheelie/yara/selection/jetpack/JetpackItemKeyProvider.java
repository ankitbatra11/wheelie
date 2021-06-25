package com.abatra.android.wheelie.yara.selection.jetpack;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.selection.ItemKeyProvider;

import com.abatra.android.wheelie.yara.selection.SelectableRecyclerViewItem;
import com.abatra.android.wheelie.yara.selection.SelectionRecyclerViewAdapter;

import java.util.Optional;

class JetpackItemKeyProvider<K> extends ItemKeyProvider<K> {

    private final SelectionRecyclerViewAdapter<?> recyclerViewAdapter;

    private JetpackItemKeyProvider(int scope, SelectionRecyclerViewAdapter<?> recyclerViewAdapter) {
        super(scope);
        this.recyclerViewAdapter = recyclerViewAdapter;
    }

    static <K> JetpackItemKeyProvider<K> cached(SelectionRecyclerViewAdapter<?> recyclerViewAdapter) {
        return new JetpackItemKeyProvider<>(SCOPE_CACHED, recyclerViewAdapter);
    }

    @Nullable
    @Override
    public K getKey(int position) {
        //noinspection unchecked
        return (K) Optional.ofNullable(recyclerViewAdapter.getItem(position))
                .filter(item -> item instanceof SelectableRecyclerViewItem)
                .map(item -> ((SelectableRecyclerViewItem) item))
                .map(SelectableRecyclerViewItem::getKey)
                .orElse(null);
    }

    @Override
    public int getPosition(@NonNull K key) {
        return recyclerViewAdapter.getPositionOfKey(key);
    }
}
