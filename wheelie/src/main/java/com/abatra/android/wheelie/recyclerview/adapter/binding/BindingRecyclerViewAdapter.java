package com.abatra.android.wheelie.recyclerview.adapter.binding;

import com.abatra.android.wheelie.recyclerview.adapter.RecyclerViewAdapter;
import com.abatra.android.wheelie.recyclerview.adapter.RecyclerViewItemViewTypeFactory;
import com.abatra.android.wheelie.recyclerview.adapter.RecyclerViewItems;

public class BindingRecyclerViewAdapter extends RecyclerViewAdapter<BindingViewHolder> {

    public BindingRecyclerViewAdapter(RecyclerViewItems recyclerViewItems,
                                      RecyclerViewItemViewTypeFactory itemViewTypeFactory) {
        super(recyclerViewItems, itemViewTypeFactory);
    }
}
