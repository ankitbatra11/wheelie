package com.abatra.android.wheelie.chronicle.model;

import java.util.ArrayList;
import java.util.List;

public class BeginCheckoutEventParams {

    private String coupon;
    private Price price;
    private final List<PurchasableItem> items = new ArrayList<>();

    public String getCoupon() {
        return coupon;
    }

    public BeginCheckoutEventParams setCoupon(String coupon) {
        this.coupon = coupon;
        return this;
    }

    public Price getPrice() {
        return price;
    }

    public BeginCheckoutEventParams setPrice(Price price) {
        this.price = price;
        return this;
    }

    public BeginCheckoutEventParams setItems(List<PurchasableItem> items) {
        this.items.clear();
        this.items.addAll(items);
        return this;
    }

    public List<PurchasableItem> getItems() {
        return items;
    }

    public BeginCheckoutEventParams addItem(PurchasableItem item) {
        items.add(item);
        return this;
    }
}
