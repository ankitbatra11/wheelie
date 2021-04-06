package com.abatra.android.wheelie.recyclerview.adapter;

import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

public interface RecyclerViewItemViewType {

    int getValue();

    <VH extends RecyclerView.ViewHolder> VH createViewHolder(ViewGroup parent);
}
