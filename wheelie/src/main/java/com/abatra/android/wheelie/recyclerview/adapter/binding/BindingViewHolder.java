package com.abatra.android.wheelie.recyclerview.adapter.binding;

import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

public class BindingViewHolder extends RecyclerView.ViewHolder {

    private final ViewBinding viewBinding;

    public BindingViewHolder(ViewBinding viewBinding) {
        super(viewBinding.getRoot());
        this.viewBinding = viewBinding;
    }

    public <T extends ViewBinding> T getViewBinding() {
        //noinspection unchecked
        return (T) viewBinding;
    }
}
