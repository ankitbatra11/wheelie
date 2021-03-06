package com.abatra.android.wheelie.chronicle.firebase;

import java.util.ArrayList;
import java.util.List;

public class FirebaseCheckoutEventParams implements FirebasePriceParam<FirebaseCheckoutEventParams> {

    private String coupon;
    private FirebasePrice price;
    private final List<FirebasePurchasableItem> items = new ArrayList<>();

    String getCoupon() {
        return coupon;
    }

    public FirebaseCheckoutEventParams setCoupon(String coupon) {
        this.coupon = coupon;
        return this;
    }

    @Override
    public FirebaseCheckoutEventParams setFirebasePrice(FirebasePrice firebasePrice) {
        price = firebasePrice;
        return this;
    }

    @Override
    public FirebasePrice getFirebasePrice() {
        return price;
    }

    public FirebaseCheckoutEventParams addCheckedOutItem(FirebasePurchasableItem item) {
        items.add(item);
        return this;
    }

    List<FirebasePurchasableItem> getItems() {
        return items;
    }
}
