package com.abatra.android.wheelie.chronicle.firebase;

import com.abatra.android.wheelie.chronicle.PurchaseEventParams;

import java.util.ArrayList;
import java.util.List;

public class FirebasePurchaseEventParams implements PurchaseEventParams, FirebasePriceParam<FirebasePurchaseEventParams> {

    private String transactionId;
    private String affiliation;
    private FirebasePrice price;
    private double tax;
    private double shipping;
    private String coupon;
    private final List<FirebasePurchasableItem> items = new ArrayList<>();

    String getTransactionId() {
        return transactionId;
    }

    public FirebasePurchaseEventParams setTransactionId(String transactionId) {
        this.transactionId = transactionId;
        return this;
    }

    String getAffiliation() {
        return affiliation;
    }

    public FirebasePurchaseEventParams setAffiliation(String affiliation) {
        this.affiliation = affiliation;
        return this;
    }

    double getTax() {
        return tax;
    }

    public FirebasePurchaseEventParams setTax(double tax) {
        this.tax = tax;
        return this;
    }

    double getShipping() {
        return shipping;
    }

    public FirebasePurchaseEventParams setShipping(double shipping) {
        this.shipping = shipping;
        return this;
    }

    String getCoupon() {
        return coupon;
    }

    public FirebasePurchaseEventParams setCoupon(String coupon) {
        this.coupon = coupon;
        return this;
    }

    @Override
    public FirebasePurchaseEventParams setFirebasePrice(FirebasePrice firebasePrice) {
        price = firebasePrice;
        return this;
    }

    @Override
    public FirebasePrice getFirebasePrice() {
        return price;
    }

    public FirebasePurchaseEventParams addPurchasedItem(FirebasePurchasableItem firebasePurchasableItem) {
        items.add(firebasePurchasableItem);
        return this;
    }

    List<FirebasePurchasableItem> getItems() {
        return items;
    }

    @Override
    public String toString() {
        return "FirebasePurchaseEventParams{" +
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
