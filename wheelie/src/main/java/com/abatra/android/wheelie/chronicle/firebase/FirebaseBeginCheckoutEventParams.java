package com.abatra.android.wheelie.chronicle.firebase;

import com.abatra.android.wheelie.chronicle.BeginCheckoutEventParams;

import java.util.ArrayList;
import java.util.List;

public class FirebaseBeginCheckoutEventParams implements BeginCheckoutEventParams,
        FirebasePriceParam<FirebaseBeginCheckoutEventParams> {

    private String coupon;
    private FirebasePrice price;
    private final List<FirebasePurchasableItem> items = new ArrayList<>();

    String getCoupon() {
        return coupon;
    }

    public FirebaseBeginCheckoutEventParams setCoupon(String coupon) {
        this.coupon = coupon;
        return this;
    }

    @Override
    public FirebaseBeginCheckoutEventParams setFirebasePrice(FirebasePrice firebasePrice) {
        price = firebasePrice;
        return this;
    }

    @Override
    public FirebasePrice getFirebasePrice() {
        return price;
    }

    public FirebaseBeginCheckoutEventParams addCheckedOutItem(FirebasePurchasableItem item) {
        items.add(item);
        return this;
    }

    List<FirebasePurchasableItem> getItems() {
        return items;
    }
}
