package com.abatra.android.wheelie.recyclerview.adapter;

abstract public class AbstractRecyclerViewItemViewType implements RecyclerViewItemViewType {

    private final int value;

    public AbstractRecyclerViewItemViewType(int value) {
        this.value = value;
    }

    @Override
    public int getValue() {
        return value;
    }
}
