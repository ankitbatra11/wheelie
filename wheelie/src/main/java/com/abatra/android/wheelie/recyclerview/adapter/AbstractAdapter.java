package com.abatra.android.wheelie.recyclerview.adapter;

import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Predicate;

abstract public class AbstractAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH>
        implements RecyclerViewItem.Api, Adapter {

    private final RecyclerViewItem.Api delegate;

    protected AbstractAdapter(RecyclerViewItem.Api delegate) {
        this.delegate = delegate;
    }

    @Override
    public int addItem(RecyclerViewItem item) {
        int position = delegate.addItem(item);
        notifyItemInserted(position);
        return position;
    }

    @Override
    public void addItemAt(int pos, RecyclerViewItem item) {
        delegate.addItemAt(pos, item);
        notifyItemInserted(pos);
    }

    @Override
    public int addItems(List<? extends RecyclerViewItem> items) {
        int start = delegate.addItems(items);
        notifyItemRangeInserted(start, items.size());
        return start;
    }

    @Override
    public void removeItems() {
        int size = delegate.getItems().size();
        delegate.removeItems();
        notifyItemRangeRemoved(0, size);
    }

    @Override
    public void setItems(List<? extends RecyclerViewItem> items) {
        delegate.setItems(items);
        notifyDataSetChanged();
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
            notifyItemChanged(updatedItemIndex);
        }
        return updatedItemIndex;
    }

    @Override
    public int removeItem(Predicate<RecyclerViewItem> itemPredicate) {
        int pos = delegate.removeItem(itemPredicate);
        if (pos >= 0) {
            notifyItemRemoved(pos);
        }
        return pos;
    }

    @Override
    public void removeItem(int itemPos) {
        delegate.removeItem(itemPos);
        notifyItemRemoved(itemPos);
    }

    @Override
    public void moveItem(int fromPos, int toPos) {
        delegate.moveItem(fromPos, toPos);
        notifyItemMoved(fromPos, toPos);
    }

    @Override
    public int getPositionOfItem(Predicate<RecyclerViewItem> singleMatchPredicate) {
        return delegate.getPositionOfItem(singleMatchPredicate);
    }

    @Override
    public List<RecyclerViewItem> getItems(Predicate<RecyclerViewItem> recyclerViewItemPredicate) {
        return delegate.getItems(recyclerViewItemPredicate);
    }

    @Override
    public int getItemCount() {
        return delegate.getItems().size();
    }

    protected RecyclerViewItem.Api getItemApi() {
        return delegate;
    }

    @Override
    public void dispatchUpdates(DiffUtil.DiffResult diffResult) {
        diffResult.dispatchUpdatesTo(this);
    }
}
