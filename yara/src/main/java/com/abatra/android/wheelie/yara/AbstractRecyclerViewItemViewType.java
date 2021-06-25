package com.abatra.android.wheelie.yara;

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
