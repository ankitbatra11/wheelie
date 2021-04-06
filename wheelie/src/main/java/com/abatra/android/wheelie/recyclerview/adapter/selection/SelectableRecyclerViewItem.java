package com.abatra.android.wheelie.recyclerview.adapter.selection;

import androidx.annotation.Nullable;

import com.abatra.android.wheelie.recyclerview.adapter.RecyclerViewItem;

import java.util.List;

public interface SelectableRecyclerViewItem extends RecyclerViewItem {

    @Nullable
    default Object getKey() {
        return null;
    }

    boolean isSelected();

    void setSelected(boolean selected);

    interface Api {

        int getPositionOfKey(Object key);

        void clearSelection();

        int getSelectedItemCount();

        boolean isSelected(SelectableRecyclerViewItem selectableRecyclerViewItem);

        List<RecyclerViewItem> getSelectedItems();

        void selectAll();

        List<?> getSelectedItemKeys();

        boolean hasSelection();
    }
}
