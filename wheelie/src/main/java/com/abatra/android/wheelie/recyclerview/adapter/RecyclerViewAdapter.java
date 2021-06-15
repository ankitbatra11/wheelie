package com.abatra.android.wheelie.recyclerview.adapter;

import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.abatra.android.wheelie.lifecycle.owner.ILifecycleOwner;

import java.util.Optional;

import timber.log.Timber;

public class RecyclerViewAdapter<VH extends RecyclerView.ViewHolder> extends AbstractAdapter<VH> implements Adapter {

    private final RecyclerViewItemViewTypeFactory itemViewTypeFactory;
    private final SparseArray<OnItemChildViewClickListener> itemChildViewClickListeners = new SparseArray<>();
    private final CompositeDataObserver compositeDataObserver = new CompositeDataObserver();
    @Nullable
    private RecyclerView recyclerView;
    @Nullable
    private OnItemClickListener onItemClickListener;
    @Nullable
    private OnItemLongClickListener onItemLongClickListener;

    public RecyclerViewAdapter(RecyclerViewItems recyclerViewItems, RecyclerViewItemViewTypeFactory itemViewTypeFactory) {
        super(recyclerViewItems);
        this.itemViewTypeFactory = itemViewTypeFactory;
        registerAdapterDataObserver(compositeDataObserver);
    }

    @Override
    public void observeLifecycle(ILifecycleOwner lifecycleOwner) {
        lifecycleOwner.getLifecycle().addObserver(this);
    }

    @Override
    public void setOnItemClickListener(@Nullable OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public void setOnItemLongClickListener(@Nullable OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    @Override
    public void setItemChildViewClickListener(@IdRes int childViewId, @Nullable OnItemChildViewClickListener onClickListener) {
        if (onClickListener != null) {
            itemChildViewClickListeners.put(childViewId, onClickListener);
        } else {
            itemChildViewClickListeners.remove(childViewId);
        }
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

    public void setRecyclerView(@Nullable RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        VH viewHolder = itemViewTypeFactory.createItemViewType(viewType).createViewHolder(parent);

        assert recyclerView != null;

        Optional.ofNullable(onItemClickListener).ifPresent(onItemClickListener -> {
            View itemView = viewHolder.itemView;
            itemView.setOnClickListener(v -> {
                int pos = recyclerView.getChildAdapterPosition(v);
                getOptionalItem(pos).ifPresent(item -> onItemClickListener.onItemClick(v, pos, item));
            });
        });
        Optional.ofNullable(onItemLongClickListener).ifPresent(onItemLongClickListener -> {
            View itemView = viewHolder.itemView;
            itemView.setOnLongClickListener(v -> {
                int pos = recyclerView.getChildAdapterPosition(v);
                return getOptionalItem(pos)
                        .map(item -> onItemLongClickListener.onItemLongClick(v, pos, item))
                        .orElse(false);
            });
        });
        for (int i = 0; i < itemChildViewClickListeners.size(); i++) {
            int childViewId = itemChildViewClickListeners.keyAt(i);
            Optional<View> optionalChildView = Optional.ofNullable(viewHolder.itemView.findViewById(childViewId));
            final int itemPos = i;
            optionalChildView.ifPresent(childView -> {
                final OnItemChildViewClickListener viewClickListener = itemChildViewClickListeners.valueAt(itemPos);
                childView.setOnClickListener(v -> {
                    int pos = recyclerView.getChildAdapterPosition((View) v.getParent());
                    getOptionalItem(pos).ifPresent(item -> viewClickListener.onItemChildViewClick(v, pos, item));
                });
            });
        }
        return viewHolder;
    }

    private Optional<RecyclerViewItem> getOptionalItem(int pos) {
        RecyclerViewItem recyclerViewItem = null;
        if (pos != RecyclerView.NO_POSITION) {
            try {
                recyclerViewItem = getItemApi().getItem(pos);
            } catch (IndexOutOfBoundsException e) {
                Timber.e(e);
            }
        }
        return Optional.ofNullable(recyclerViewItem);
    }

    @Override
    public int getItemViewType(int position) {
        return getItemApi().getItem(position).getViewType().getValue();
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        getItemApi().getItem(position).bind(holder, position);
    }

    @Override
    public void registerNoItemsViewDataObserver(View noItemsView) {
        compositeDataObserver.addObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                noItemsView.setVisibility(getItemCount() > 0 ? View.GONE : View.VISIBLE);
            }
        });
    }

    @Override
    public void onDestroy() {
        unregisterAdapterDataObserver(compositeDataObserver);
        recyclerView = null;
    }
}
