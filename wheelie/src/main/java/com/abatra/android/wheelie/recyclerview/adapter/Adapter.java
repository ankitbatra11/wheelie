package com.abatra.android.wheelie.recyclerview.adapter;

import android.view.View;

import androidx.annotation.IdRes;
import androidx.annotation.Nullable;

import com.abatra.android.wheelie.lifecycle.observer.ILifecycleObserver;

public interface Adapter extends ILifecycleObserver, RecyclerViewItem.Api {

    void setOnItemClickListener(@Nullable OnItemClickListener onItemClickListener);

    void setOnItemLongClickListener(@Nullable OnItemLongClickListener onItemLongClickListener);

    void setItemChildViewClickListener(@IdRes int childViewId, @Nullable OnItemChildViewClickListener onClickListener);

    void registerNoItemsViewDataObserver(View noItemsView);
}
