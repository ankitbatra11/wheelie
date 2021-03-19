package com.abatra.android.wheelie.chronicle.model;

import androidx.annotation.NonNull;

public class PurchasableItem {

    private final Item item = new Item();
    private String category;
    private String variant;
    private String brand;
    private double price;
    private long quantity;

    public PurchasableItem setId(String id) {
        item.setId(id);
        return this;
    }

    public PurchasableItem setName(String name) {
        item.setName(name);
        return this;
    }

    public PurchasableItem setCategory(String category) {
        this.category = category;
        return this;
    }

    public PurchasableItem setVariant(String variant) {
        this.variant = variant;
        return this;
    }

    public PurchasableItem setBrand(String brand) {
        this.brand = brand;
        return this;
    }

    public PurchasableItem setPrice(double price) {
        this.price = price;
        return this;
    }

    public PurchasableItem setQuantity(long quantity) {
        this.quantity = quantity;
        return this;
    }

    public String getId() {
        return item.getId();
    }

    public String getName() {
        return item.getName();
    }

    public String getCategory() {
        return category;
    }

    public String getVariant() {
        return variant;
    }

    public String getBrand() {
        return brand;
    }

    public double getPrice() {
        return price;
    }

    public long getQuantity() {
        return quantity;
    }

    @NonNull
    @Override
    public String toString() {
        return "PurchasableItem{" +
                "item=" + item +
                ", category='" + category + '\'' +
                ", variant='" + variant + '\'' +
                ", brand='" + brand + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                '}';
    }
}
