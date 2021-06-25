package com.abatra.android.wheelie.yara.binding;

import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import com.abatra.android.wheelie.yara.RecyclerViewItem;
import com.abatra.android.wheelie.yara.RecyclerViewItemViewType;

public abstract class ViewBindingRecyclerViewItem<VB extends ViewBinding> implements RecyclerViewItem {

    private final RecyclerViewItemViewType viewType;

    protected ViewBindingRecyclerViewItem(RecyclerViewItemViewType viewType) {
        this.viewType = viewType;
    }

    @Override
    public RecyclerViewItemViewType getViewType() {
        return viewType;
    }

    @Override
    public <VH extends RecyclerView.ViewHolder> void bind(VH viewHolder, int pos) {
        BindingViewHolder bindingViewHolder = (BindingViewHolder) viewHolder;
        VB binding = bindingViewHolder.getViewBinding();
        bind(binding, pos);
    }

    protected abstract void bind(VB binding, int pos);
}
