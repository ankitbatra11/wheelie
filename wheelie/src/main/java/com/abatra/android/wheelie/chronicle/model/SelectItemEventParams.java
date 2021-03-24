package com.abatra.android.wheelie.chronicle.model;

public class SelectItemEventParams {

    private String itemListId;
    private String itemListName;
    private Item item;

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

    public Item getItem() {
        return item;
    }

    public SelectItemEventParams setItem(Item item) {
        this.item = item;
        return this;
    }
}
