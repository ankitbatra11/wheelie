package com.abatra.android.wheelie.yara;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Predicate;

public interface RecyclerViewItem {

    RecyclerViewItemViewType getViewType();

    <VH extends RecyclerView.ViewHolder> void bind(VH viewHolder, int pos);

    interface Api {

        int addItem(RecyclerViewItem item);

        void addItemAt(int pos, RecyclerViewItem item);

        int removeItem(Predicate<RecyclerViewItem> itemPredicate);

        void removeItem(int itemPos);

        RecyclerViewItem getItem(int pos);

        int addItems(List<? extends RecyclerViewItem> items);

        void removeItems();

        void setItems(List<? extends RecyclerViewItem> items);

        int updateItem(RecyclerViewItem recyclerViewItem, BiFunction<RecyclerViewItem, RecyclerViewItem, Boolean> equalsFunction);

        void moveItem(int fromPos, int toPos);

        List<RecyclerViewItem> getItems();

        int getPositionOfItem(Predicate<RecyclerViewItem> singleMatchPredicate);

        List<RecyclerViewItem> getItems(Predicate<RecyclerViewItem> recyclerViewItemPredicate);
    }
}
