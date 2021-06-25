package com.abatra.android.wheelie.yara;

import androidx.recyclerview.widget.RecyclerView;

import com.google.common.collect.Lists;

import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class RecyclerViewItems implements RecyclerViewItem.Api {

    private final List<RecyclerViewItem> recyclerViewItems;

    public RecyclerViewItems(List<RecyclerViewItem> recyclerViewItems) {
        this.recyclerViewItems = recyclerViewItems;
    }

    public static RecyclerViewItems singleItem(RecyclerViewItem recyclerViewItem) {
        return new RecyclerViewItems(Lists.newArrayList(recyclerViewItem));
    }

    public static RecyclerViewItems empty() {
        return new RecyclerViewItems(Lists.newArrayList());
    }

    public static RecyclerViewItems fromArray(RecyclerViewItem... recyclerViewItems) {
        return new RecyclerViewItems(Lists.newArrayList(recyclerViewItems));
    }

    public int getItemCount() {
        return recyclerViewItems.size();
    }

    @Override
    public RecyclerViewItem getItem(int position) {
        return recyclerViewItems.get(position);
    }

    @Override
    public List<RecyclerViewItem> getItems() {
        return recyclerViewItems;
    }

    @Override
    public int updateItem(RecyclerViewItem recyclerViewItem,
                          BiFunction<RecyclerViewItem, RecyclerViewItem, Boolean> equalsFunction) {
        for (int i = 0; i < recyclerViewItems.size(); i++) {
            if (equalsFunction.apply(recyclerViewItem, recyclerViewItems.get(i))) {
                recyclerViewItems.set(i, recyclerViewItem);
                return i;
            }
        }
        return -1;
    }

    @Override
    public int removeItem(Predicate<RecyclerViewItem> itemPredicate) {
        for (int i = 0; i < recyclerViewItems.size(); i++) {
            RecyclerViewItem item = recyclerViewItems.get(i);
            if (itemPredicate.test(item)) {
                recyclerViewItems.remove(item);
                return i;
            }
        }
        return -1;
    }

    @Override
    public void removeItem(int itemPos) {
        recyclerViewItems.remove(itemPos);
    }

    @Override
    public void addItemAt(int pos, RecyclerViewItem item) {
        recyclerViewItems.add(pos, item);
    }

    @Override
    public void moveItem(int fromPos, int toPos) {
        Collections.swap(recyclerViewItems, fromPos, toPos);
    }

    @Override
    public int getPositionOfItem(Predicate<RecyclerViewItem> singleMatchPredicate) {
        for (int i = 0; i < recyclerViewItems.size(); i++) {
            if (singleMatchPredicate.test(recyclerViewItems.get(i))) {
                return i;
            }
        }
        return RecyclerView.NO_POSITION;
    }

    @Override
    public List<RecyclerViewItem> getItems(Predicate<RecyclerViewItem> recyclerViewItemPredicate) {
        return getItems().stream()
                .filter(recyclerViewItemPredicate)
                .collect(Collectors.toList());
    }

    @Override
    public int addItem(RecyclerViewItem item) {
        int result = recyclerViewItems.size();
        recyclerViewItems.add(item);
        return result;
    }

    @Override
    public int addItems(List<? extends RecyclerViewItem> items) {
        int result = recyclerViewItems.size();
        recyclerViewItems.addAll(items);
        return result;
    }

    @Override
    public void removeItems() {
        recyclerViewItems.clear();
    }

    @Override
    public void setItems(List<? extends RecyclerViewItem> items) {
        recyclerViewItems.clear();
        recyclerViewItems.addAll(items);
    }
}
