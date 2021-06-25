package com.abatra.android.wheelie.demo;

import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.abatra.android.wheelie.demo.databinding.RecyclerViewAdapterDemoActivityBinding;
import com.abatra.android.wheelie.demo.databinding.RecyclerViewAdapterDemoItemBinding;
import com.abatra.android.wheelie.core.res.text.Text;
import com.abatra.android.wheelie.yara.RecyclerViewAdapter;
import com.abatra.android.wheelie.yara.RecyclerViewItem;
import com.abatra.android.wheelie.yara.RecyclerViewItemViewTypeFactory;
import com.abatra.android.wheelie.yara.RecyclerViewItems;
import com.abatra.android.wheelie.yara.binding.BindingRecyclerViewItemViewType;
import com.abatra.android.wheelie.yara.binding.ViewBindingRecyclerViewItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class RecyclerViewAdapterDemoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RecyclerViewAdapterDemoActivityBinding binding = RecyclerViewAdapterDemoActivityBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<RecyclerViewItem> oldItems = new ArrayList<>();
        RecyclerViewAdapter<RecyclerView.ViewHolder> adapter = new RecyclerViewAdapter<>(
                new RecyclerViewItems(oldItems),
                RecyclerViewItemViewTypeFactory.singleItemViewType(ListItem.VIEW_TYPE)
        );
        binding.recyclerView.setAdapter(adapter);

        Executors.newSingleThreadScheduledExecutor().schedule(
                () -> {
                    List<RecyclerViewItem> items = Arrays.asList(
                            new ListItem(Text.string("One")),
                            new ListItem(Text.string("Two")),
                            new ListItem(Text.string("Three"))
                    );
                    DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffUtil.Callback() {

                        @Override
                        public int getOldListSize() {
                            return oldItems.size();
                        }

                        @Override
                        public int getNewListSize() {
                            return items.size();
                        }

                        @Override
                        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                            return oldItems.get(oldItemPosition) == items.get(newItemPosition);
                        }

                        @Override
                        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                            return oldItems.get(oldItemPosition).toString().equals(items.get(newItemPosition).toString());
                        }
                    });

                    runOnUiThread(() -> adapter.setItems(items, diffResult));
                },
                2,
                TimeUnit.SECONDS
        );
    }

    private static class ListItem extends ViewBindingRecyclerViewItem<RecyclerViewAdapterDemoItemBinding> {

        public static final BindingRecyclerViewItemViewType VIEW_TYPE = new BindingRecyclerViewItemViewType(0,
                (layoutInflater, viewGroup) -> RecyclerViewAdapterDemoItemBinding.inflate(layoutInflater, viewGroup, false));

        private final Text text;

        protected ListItem(Text text) {
            super(VIEW_TYPE);
            this.text = text;
        }

        @Override
        protected void bind(RecyclerViewAdapterDemoItemBinding binding, int pos) {
            text.show(binding.text);
        }
    }
}
