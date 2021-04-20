package com.abatra.android.wheelie.chronicle.model;

public class SelectItemEventParams {

    private String itemListId;
    private String itemListName;
    private Item item;

    public static SelectItemEventParams create(String listName, String itemName) {
        return new SelectItemEventParams()
                .setItemListName(listName)
                .setItem(Item.ofName(itemName));
    }

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
