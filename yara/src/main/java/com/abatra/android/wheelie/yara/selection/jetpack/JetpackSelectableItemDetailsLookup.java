package com.abatra.android.wheelie.yara.selection.jetpack;

import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.selection.ItemDetailsLookup;
import androidx.recyclerview.selection.ItemKeyProvider;
import androidx.recyclerview.widget.RecyclerView;

class JetpackSelectableItemDetailsLookup<K> extends ItemDetailsLookup<K> {

    private final RecyclerView recyclerView;
    private final ItemKeyProvider<K> itemKeyProvider;

    JetpackSelectableItemDetailsLookup(RecyclerView recyclerView, ItemKeyProvider<K> itemKeyProvider) {
        this.recyclerView = recyclerView;
        this.itemKeyProvider = itemKeyProvider;
    }

    @Nullable
    @Override
    public ItemDetails<K> getItemDetails(@NonNull MotionEvent e) {
        ItemDetails<K> result = null;
        View view = recyclerView.findChildViewUnder(e.getX(), e.getY());
        if (view != null) {
            int pos = recyclerView.getChildAdapterPosition(view);
            if (pos != RecyclerView.NO_POSITION) {
                result = new JetpackSelectableItemDetails<>(itemKeyProvider, pos);
            }
        }
        return result;
    }
}
