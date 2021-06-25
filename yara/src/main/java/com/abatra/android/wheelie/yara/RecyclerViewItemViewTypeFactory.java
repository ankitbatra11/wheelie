package com.abatra.android.wheelie.yara;

import java.util.HashMap;
import java.util.Map;

public interface RecyclerViewItemViewTypeFactory {

    static RecyclerViewItemViewTypeFactory singleItemViewType(RecyclerViewItemViewType itemViewType) {
        return value -> itemViewType;
    }

    static RecyclerViewItemViewTypeFactory multipleItemViewTypes(RecyclerViewItemViewType... types) {
        Map<Integer, RecyclerViewItemViewType> map = new HashMap<>();
        for (RecyclerViewItemViewType type : types) {
            map.put(type.getValue(), type);
        }
        return new HashMapRecyclerViewItemViewTypeFactory(map);
    }

    RecyclerViewItemViewType createItemViewType(int value);
}
