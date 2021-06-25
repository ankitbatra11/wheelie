package com.abatra.android.wheelie.yara;

import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

public interface RecyclerViewItemViewType {

    int getValue();

    <VH extends RecyclerView.ViewHolder> VH createViewHolder(ViewGroup parent);
}
