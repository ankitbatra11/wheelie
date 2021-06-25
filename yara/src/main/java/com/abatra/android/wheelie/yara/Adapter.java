package com.abatra.android.wheelie.yara;

import android.view.View;

import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import com.abatra.android.wheelie.lifecycle.observer.ILifecycleObserver;

import java.util.List;

public interface Adapter extends ILifecycleObserver, RecyclerViewItem.Api {

    void setOnItemClickListener(@Nullable OnItemClickListener onItemClickListener);

    void setOnItemLongClickListener(@Nullable OnItemLongClickListener onItemLongClickListener);

    void setItemChildViewClickListener(@IdRes int childViewId, @Nullable OnItemChildViewClickListener onClickListener);

    void registerNoItemsViewDataObserver(View noItemsView);

    void setItems(List<RecyclerViewItem> items, DiffUtil.DiffResult diffResult);

    void dispatchUpdates(DiffUtil.DiffResult diffResult);
}
