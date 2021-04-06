package com.abatra.android.wheelie.recyclerview.adapter;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Predicate;

public class AdapterItemApi implements RecyclerViewItem.Api {

    private final RecyclerViewItem.Api delegate;
    private final RecyclerViewAdapter<?> recyclerViewAdapter;

    protected AdapterItemApi(RecyclerViewItem.Api delegate, RecyclerViewAdapter<?> recyclerViewAdapter) {
        this.delegate = delegate;
        this.recyclerViewAdapter = recyclerViewAdapter;
    }

    @Override
    public int addItem(RecyclerViewItem item) {
        int position = delegate.addItem(item);
        recyclerViewAdapter.notifyItemInserted(position);
        return position;
    }

    @Override
    public void addItemAt(int pos, RecyclerViewItem item) {
        delegate.addItemAt(pos, item);
        recyclerViewAdapter.notifyItemInserted(pos);
    }

    @Override
    public int addItems(List<? extends RecyclerViewItem> items) {
        int start = delegate.addItems(items);
        recyclerViewAdapter.notifyItemRangeInserted(start, items.size());
        return start;
    }

    @Override
    public void removeItems() {
        int size = delegate.getItems().size();
        delegate.removeItems();
        recyclerViewAdapter.notifyItemRangeRemoved(0, size);
    }

    @Override
    public void setItems(List<? extends RecyclerViewItem> items) {
        delegate.setItems(items);
        recyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public List<RecyclerViewItem> getItems() {
        return delegate.getItems();
    }

    @Override
    public RecyclerViewItem getItem(int pos) {
        return delegate.getItem(pos);
    }

    @Override
    public int updateItem(RecyclerViewItem recyclerViewItem,
                          BiFunction<RecyclerViewItem, RecyclerViewItem, Boolean> equalsFunction) {
        int updatedItemIndex = delegate.updateItem(recyclerViewItem, equalsFunction);
        if (updatedItemIndex >= 0) {
            recyclerViewAdapter.notifyItemChanged(updatedItemIndex);
        }
        return updatedItemIndex;
    }

    @Override
    public int removeItem(Predicate<RecyclerViewItem> itemPredicate) {
        int pos = delegate.removeItem(itemPredicate);
        if (pos >= 0) {
            recyclerViewAdapter.notifyItemRemoved(pos);
        }
        return pos;
    }

    @Override
    public void removeItem(int itemPos) {
        delegate.removeItem(itemPos);
        recyclerViewAdapter.notifyItemRemoved(itemPos);
    }

    @Override
    public void moveItem(int fromPos, int toPos) {
        delegate.moveItem(fromPos, toPos);
        recyclerViewAdapter.notifyItemMoved(fromPos, toPos);
    }

    @Override
    public int getPositionOfItem(Predicate<RecyclerViewItem> singleMatchPredicate) {
        return delegate.getPositionOfItem(singleMatchPredicate);
    }

    @Override
    public List<RecyclerViewItem> getItems(Predicate<RecyclerViewItem> recyclerViewItemPredicate) {
        return delegate.getItems(recyclerViewItemPredicate);
    }
}
