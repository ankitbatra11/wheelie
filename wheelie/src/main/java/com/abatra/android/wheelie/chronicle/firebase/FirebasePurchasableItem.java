package com.abatra.android.wheelie.chronicle.firebase;

public class FirebasePurchasableItem {

    private String id;
    private String name;
    private String category;
    private String variant;
    private String brand;
    private double price;
    private long quantity;

    public FirebasePurchasableItem setId(String id) {
        this.id = id;
        return this;
    }

    public FirebasePurchasableItem setName(String name) {
        this.name = name;
        return this;
    }

    public FirebasePurchasableItem setCategory(String category) {
        this.category = category;
        return this;
    }

    public FirebasePurchasableItem setVariant(String variant) {
        this.variant = variant;
        return this;
    }

    public FirebasePurchasableItem setBrand(String brand) {
        this.brand = brand;
        return this;
    }

    public FirebasePurchasableItem setPrice(double price) {
        this.price = price;
        return this;
    }

    public FirebasePurchasableItem setQuantity(long quantity) {
        this.quantity = quantity;
        return this;
    }

    String getId() {
        return id;
    }

    String getName() {
        return name;
    }

    String getCategory() {
        return category;
    }

    String getVariant() {
        return variant;
    }

    String getBrand() {
        return brand;
    }

    double getPrice() {
        return price;
    }

    long getQuantity() {
        return quantity;
    }
}
