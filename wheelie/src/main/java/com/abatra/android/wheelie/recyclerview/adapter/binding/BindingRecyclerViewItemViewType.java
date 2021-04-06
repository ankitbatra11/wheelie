package com.abatra.android.wheelie.recyclerview.adapter.binding;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import com.abatra.android.wheelie.recyclerview.adapter.AbstractRecyclerViewItemViewType;

import java.util.function.BiFunction;

public class BindingRecyclerViewItemViewType extends AbstractRecyclerViewItemViewType {

    private final BiFunction<LayoutInflater, ViewGroup, ViewBinding> viewDataBindingFactory;

    public BindingRecyclerViewItemViewType(int value, BiFunction<LayoutInflater, ViewGroup, ViewBinding> viewDataBindingFactory) {
        super(value);
        this.viewDataBindingFactory = viewDataBindingFactory;
    }

    @Override
    public <VH extends RecyclerView.ViewHolder> VH createViewHolder(ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        //noinspection unchecked
        return (VH) new BindingViewHolder(viewDataBindingFactory.apply(layoutInflater, parent));
    }
}
