package com.abatra.android.wheelie.yara.selection;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.abatra.android.wheelie.lifecycle.owner.ILifecycleOwner;
import com.abatra.android.wheelie.yara.AbstractAdapter;
import com.abatra.android.wheelie.yara.OnItemChildViewClickListener;
import com.abatra.android.wheelie.yara.OnItemClickListener;
import com.abatra.android.wheelie.yara.OnItemLongClickListener;
import com.abatra.android.wheelie.yara.RecyclerViewAdapter;
import com.abatra.android.wheelie.yara.RecyclerViewItem;

import java.util.List;
import java.util.function.Predicate;

public class SelectionRecyclerViewAdapter<VH extends RecyclerView.ViewHolder> extends AbstractAdapter<VH>
        implements SelectableRecyclerViewItem.Api {

    private final RecyclerViewAdapter<VH> recyclerViewAdapter;
    private RecyclerViewItemSelector itemSelector;

    private SelectionRecyclerViewAdapter(RecyclerViewAdapter<VH> recyclerViewAdapter) {
        super(recyclerViewAdapter);
        this.recyclerViewAdapter = recyclerViewAdapter;
    }

    public static <VH extends RecyclerView.ViewHolder> SelectionRecyclerViewAdapter<VH> wrap(RecyclerViewAdapter<VH> recyclerViewAdapter) {
        return new SelectionRecyclerViewAdapter<>(recyclerViewAdapter);
    }

    public void setItemSelector(RecyclerViewItemSelector itemSelector) {
        this.itemSelector = itemSelector;
    }

    @Override
    public void observeLifecycle(ILifecycleOwner lifecycleOwner) {
        itemSelector.observeLifecycle(lifecycleOwner);
        recyclerViewAdapter.observeLifecycle(lifecycleOwner);
    }

    @Override
    public void setOnItemClickListener(@Nullable OnItemClickListener onItemClickListener) {
        recyclerViewAdapter.setOnItemClickListener(onItemClickListener);
    }

    @Override
    public void setOnItemLongClickListener(@Nullable OnItemLongClickListener onItemLongClickListener) {
        recyclerViewAdapter.setOnItemLongClickListener(onItemLongClickListener);
    }

    @Override
    public void setItemChildViewClickListener(int childViewId, @Nullable OnItemChildViewClickListener onClickListener) {
        recyclerViewAdapter.setItemChildViewClickListener(childViewId, onClickListener);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        recyclerViewAdapter.setRecyclerView(recyclerView);
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return recyclerViewAdapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public int getItemViewType(int position) {
        return recyclerViewAdapter.getItemViewType(position);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        setSelected(recyclerViewAdapter.getItem(position));
        recyclerViewAdapter.bindViewHolder(holder, position);
    }

    private void setSelected(RecyclerViewItem recyclerViewItem) {
        SelectableRecyclerViewItem selectableRecyclerViewItem = (SelectableRecyclerViewItem) recyclerViewItem;
        selectableRecyclerViewItem.setSelected(itemSelector.isSelected(selectableRecyclerViewItem));
    }

    @Override
    public int getPositionOfKey(Object key) {
        return itemSelector.getPositionOfKey(key);
    }

    @Override
    public int getPositionOfItem(Predicate<RecyclerViewItem> singleMatchPredicate) {
        return recyclerViewAdapter.getPositionOfItem(singleMatchPredicate);
    }

    @Override
    public void clearSelection() {
        itemSelector.clearSelection();
    }

    @Override
    public int getSelectedItemCount() {
        return itemSelector.getSelectedItemCount();
    }

    @Override
    public boolean isSelected(SelectableRecyclerViewItem selectableRecyclerViewItem) {
        return itemSelector.isSelected(selectableRecyclerViewItem);
    }

    @Override
    public List<RecyclerViewItem> getSelectedItems() {
        return itemSelector.getSelectedItems();
    }

    @Override
    public void selectAll() {
        itemSelector.selectAll();
    }

    @Override
    public List<?> getSelectedItemKeys() {
        return itemSelector.getSelectedItemKeys();
    }

    @Override
    public List<RecyclerViewItem> getItems(Predicate<RecyclerViewItem> recyclerViewItemPredicate) {
        return recyclerViewAdapter.getItems(recyclerViewItemPredicate);
    }

    @Override
    public boolean hasSelection() {
        return itemSelector.hasSelection();
    }

    @Override
    public void registerNoItemsViewDataObserver(View noItemsView) {
        recyclerViewAdapter.registerNoItemsViewDataObserver(noItemsView);
    }

    @Override
    public void dispatchUpdates(DiffUtil.DiffResult diffResult) {
        recyclerViewAdapter.dispatchUpdates(diffResult);
    }

    @Override
    public void setItems(List<RecyclerViewItem> items, DiffUtil.DiffResult diffResult) {
        recyclerViewAdapter.setItems(items, diffResult);
    }
}
