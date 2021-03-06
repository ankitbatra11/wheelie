package com.abatra.android.wheelie.chronicle.firebase;

import android.os.Bundle;
import android.os.Parcelable;

import androidx.fragment.app.Fragment;

import com.abatra.android.wheelie.chronicle.CheckoutEventParams;
import com.abatra.android.wheelie.chronicle.Event;
import com.abatra.android.wheelie.chronicle.EventBuilder;
import com.abatra.android.wheelie.chronicle.EventParams;
import com.abatra.android.wheelie.chronicle.PurchaseEventParams;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.List;

import timber.log.Timber;

import static com.google.firebase.analytics.FirebaseAnalytics.Event.BEGIN_CHECKOUT;
import static com.google.firebase.analytics.FirebaseAnalytics.Event.PURCHASE;
import static com.google.firebase.analytics.FirebaseAnalytics.Param;

public class FirebaseEventBuilder extends EventBuilder<FirebaseEventBuilder> {

    private FirebaseEventBuilder() {
    }

    @Override
    protected EventParams createEventParams() {
        return EventParams.bundled();
    }

    @Override
    public Event buildScreenViewEvent(Fragment fragment) {
        return withName(FirebaseAnalytics.Event.SCREEN_VIEW)
                .withParam(Param.SCREEN_CLASS, fragment.getClass().getSimpleName())
                .withParam(Param.SCREEN_NAME, fragment.getClass().getSimpleName())
                .build();
    }

    @Override
    public Event buildBeginCheckoutEvent(CheckoutEventParams checkoutEventParams) {
        FirebaseCheckoutEventParams firebaseCheckoutEventParams = (FirebaseCheckoutEventParams) checkoutEventParams;
        return withName(BEGIN_CHECKOUT)
                .withParam(Param.COUPON, firebaseCheckoutEventParams.getCoupon())
                .withParam(Param.VALUE, firebaseCheckoutEventParams.getValue())
                .withParam(Param.CURRENCY, firebaseCheckoutEventParams.getCurrency())
                .withParam(Param.ITEMS, toParcelables(firebaseCheckoutEventParams.getItems()))
                .build();
    }

    private Parcelable[] toParcelables(List<FirebasePurchasableItem> items) {

        Parcelable[] result = items.stream()
                .map(this::createBundle)
                .toArray(Parcelable[]::new);

        if (result.length <= 0) {
            Timber.w("buildBeginCheckoutEvent called without items. items=%s", items);
        }
        return result;
    }

    private Bundle createBundle(FirebasePurchasableItem item) {
        Bundle bundle = new Bundle();
        bundle.putString(Param.ITEM_ID, item.getId());
        bundle.putString(Param.ITEM_NAME, item.getName());
        bundle.putString(Param.ITEM_CATEGORY, item.getCategory());
        bundle.putString(Param.ITEM_BRAND, item.getBrand());
        bundle.putString(Param.ITEM_VARIANT, item.getVariant());
        bundle.putDouble(Param.PRICE, item.getPrice());
        bundle.putLong(Param.QUANTITY, item.getQuantity());
        return bundle;
    }

    @Override
    public Event buildPurchaseEvent(PurchaseEventParams purchaseEventParams) {
        FirebasePurchaseEventParams firebasePurchaseEventParams = (FirebasePurchaseEventParams) purchaseEventParams;
        return withName(PURCHASE)
                .withParam(Param.TRANSACTION_ID, firebasePurchaseEventParams.getTransactionId())
                .withParam(Param.AFFILIATION, firebasePurchaseEventParams.getAffiliation())
                .withParam(Param.CURRENCY, firebasePurchaseEventParams.getCurrency())
                .withParam(Param.VALUE, firebasePurchaseEventParams.getValue())
                .withParam(Param.TAX, firebasePurchaseEventParams.getTax())
                .withParam(Param.SHIPPING, firebasePurchaseEventParams.getShipping())
                .withParam(Param.COUPON, firebasePurchaseEventParams.getCoupon())
                .withParam(Param.ITEMS, toParcelables(firebasePurchaseEventParams.getItems()))
                .build();
    }

    public static class Factory implements EventBuilder.Factory {

        public static final Factory INSTANCE = new Factory();

        private Factory() {
        }

        @Override
        public EventBuilder<?> createEventBuilder() {
            return new FirebaseEventBuilder();
        }
    }
}
