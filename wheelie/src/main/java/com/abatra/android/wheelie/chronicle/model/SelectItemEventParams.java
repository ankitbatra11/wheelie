package com.abatra.android.wheelie.chronicle.model;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class SelectItemEventParams {

    private String itemListId;
    private String itemListName;
    private List<Item> items;

    public String getItemListId() {
        return itemListId;
    }

    public SelectItemEventParams setItemListId(String itemListId) {
        this.itemListId = itemListId;
        return this;
    }

    public String getItemListName() {
        return itemListName;
    }

    public SelectItemEventParams setItemListName(String itemListName) {
        this.itemListName = itemListName;
        return this;
    }

    public List<Item> getItems() {
        return Optional.ofNullable(items).orElse(Collections.emptyList());
    }

    public SelectItemEventParams setItems(List<Item> items) {
        this.items = items;
        return this;
    }
}
