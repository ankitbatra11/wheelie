package com.abatra.android.wheelie.chronicle.model;

import java.util.ArrayList;
import java.util.List;

public class PurchaseEventParams {

    private String transactionId;
    private String affiliation;
    private Price price;
    private double tax;
    private double shipping;
    private String coupon;
    private final List<PurchasableItem> items = new ArrayList<>();

    public String getTransactionId() {
        return transactionId;
    }

    public PurchaseEventParams setTransactionId(String transactionId) {
        this.transactionId = transactionId;
        return this;
    }

    public String getAffiliation() {
        return affiliation;
    }

    public PurchaseEventParams setAffiliation(String affiliation) {
        this.affiliation = affiliation;
        return this;
    }

    public double getTax() {
        return tax;
    }

    public PurchaseEventParams setTax(double tax) {
        this.tax = tax;
        return this;
    }

    public double getShipping() {
        return shipping;
    }

    public PurchaseEventParams setShipping(double shipping) {
        this.shipping = shipping;
        return this;
    }

    public String getCoupon() {
        return coupon;
    }

    public PurchaseEventParams setCoupon(String coupon) {
        this.coupon = coupon;
        return this;
    }

    public PurchaseEventParams setPrice(Price price) {
        this.price = price;
        return this;
    }

    public Price getPrice() {
        return price;
    }

    public PurchaseEventParams addPurchasedItem(PurchasableItem purchasableItem) {
        items.add(purchasableItem);
        return this;
    }

    public List<PurchasableItem> getItems() {
        return items;
    }

    @Override
    public String toString() {
        return "PurchaseEventParams{" +
                "transactionId='" + transactionId + '\'' +
                ", affiliation='" + affiliation + '\'' +
                ", price=" + price +
                ", tax=" + tax +
                ", shipping=" + shipping +
                ", coupon='" + coupon + '\'' +
                ", items=" + items +
                '}';
    }
}
