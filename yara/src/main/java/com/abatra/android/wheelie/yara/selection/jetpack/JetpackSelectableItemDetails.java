package com.abatra.android.wheelie.yara.selection.jetpack;

import androidx.annotation.Nullable;
import androidx.recyclerview.selection.ItemDetailsLookup;
import androidx.recyclerview.selection.ItemKeyProvider;

class JetpackSelectableItemDetails<K> extends ItemDetailsLookup.ItemDetails<K> {

    private final ItemKeyProvider<K> itemKeyProvider;
    private final int pos;

    JetpackSelectableItemDetails(ItemKeyProvider<K> itemKeyProvider, int pos) {
        this.itemKeyProvider = itemKeyProvider;
        this.pos = pos;
    }

    @Override
    public int getPosition() {
        return pos;
    }

    @Nullable
    @Override
    public K getSelectionKey() {
        return itemKeyProvider.getKey(pos);
    }
}
