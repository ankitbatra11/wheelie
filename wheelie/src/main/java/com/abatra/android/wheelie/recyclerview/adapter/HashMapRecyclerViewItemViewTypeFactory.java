package com.abatra.android.wheelie.recyclerview.adapter;

import java.util.Map;

public class HashMapRecyclerViewItemViewTypeFactory implements RecyclerViewItemViewTypeFactory {

    private final Map<Integer, RecyclerViewItemViewType> map;

    HashMapRecyclerViewItemViewTypeFactory(Map<Integer, RecyclerViewItemViewType> map) {
        this.map = map;
    }

    @Override
    public RecyclerViewItemViewType createItemViewType(int value) {
        return map.get(value);
    }
}
