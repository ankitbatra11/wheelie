package com.abatra.android.wheelie.chronicle.firebase;

import android.os.Bundle;
import android.os.Parcelable;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.abatra.android.wheelie.chronicle.Event;
import com.abatra.android.wheelie.chronicle.EventBuilder;
import com.abatra.android.wheelie.chronicle.EventParams;
import com.abatra.android.wheelie.chronicle.model.BeginCheckoutEventParams;
import com.abatra.android.wheelie.chronicle.model.Item;
import com.abatra.android.wheelie.chronicle.model.Price;
import com.abatra.android.wheelie.chronicle.model.PurchasableItem;
import com.abatra.android.wheelie.chronicle.model.PurchaseEventParams;
import com.abatra.android.wheelie.chronicle.model.SelectItemEventParams;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.List;
import java.util.Optional;

import timber.log.Timber;

import static com.google.firebase.analytics.FirebaseAnalytics.Event.BEGIN_CHECKOUT;
import static com.google.firebase.analytics.FirebaseAnalytics.Event.PURCHASE;
import static com.google.firebase.analytics.FirebaseAnalytics.Event.SELECT_ITEM;
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
    public Event buildBeginCheckoutEvent(BeginCheckoutEventParams beginCheckoutEventParams) {
        return withName(BEGIN_CHECKOUT)
                .withParam(Param.COUPON, beginCheckoutEventParams.getCoupon())
                .withPriceParam(beginCheckoutEventParams.getPrice())
                .withParam(Param.ITEMS, purchasableItemsToParcelables(beginCheckoutEventParams.getItems()))
                .build();
    }

    private EventBuilder<?> withPriceParam(@Nullable Price price) {
        Optional.ofNullable(price).ifPresent(p -> {
            if (p.getCurrency() != null) {
                withParam(Param.CURRENCY, p.getCurrency());
                withParam(Param.VALUE, p.getValue());
            } else {
                Timber.w("Must specify currency with value and vice versa! price=%s", p);
            }
        });
        return this;
    }

    private Parcelable[] purchasableItemsToParcelables(List<PurchasableItem> items) {

        Parcelable[] result = items.stream()
                .map(this::createBundle)
                .toArray(Parcelable[]::new);

        if (result.length <= 0) {
            Timber.w("buildBeginCheckoutEvent called without items. items=%s", items);
        }
        return result;
    }

    private Bundle createBundle(PurchasableItem item) {
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
        return withName(PURCHASE)
                .withParam(Param.TRANSACTION_ID, purchaseEventParams.getTransactionId())
                .withParam(Param.AFFILIATION, purchaseEventParams.getAffiliation())
                .withPriceParam(purchaseEventParams.getPrice())
                .withParam(Param.TAX, purchaseEventParams.getTax())
                .withParam(Param.SHIPPING, purchaseEventParams.getShipping())
                .withParam(Param.COUPON, purchaseEventParams.getCoupon())
                .withParam(Param.ITEMS, purchasableItemsToParcelables(purchaseEventParams.getItems()))
                .build();
    }

    @Override
    public Event buildSelectItemEvent(SelectItemEventParams selectItemEventParams) {
        return withName(SELECT_ITEM)
                .withParam(Param.ITEM_LIST_ID, selectItemEventParams.getItemListId())
                .withParam(Param.ITEM_LIST_NAME, selectItemEventParams.getItemListName())
                .withParam(Param.ITEMS, itemsToParcelables(selectItemEventParams.getItems()))
                .build();
    }

    private Parcelable[] itemsToParcelables(List<Item> items) {
        return items.stream()
                .map(this::createBundle)
                .toArray(Parcelable[]::new);
    }

    private Bundle createBundle(Item item) {
        Bundle bundle = new Bundle();
        bundle.putString(Param.ITEM_ID, item.getId());
        bundle.putString(Param.ITEM_NAME, item.getName());
        return bundle;
    }

    public static class Factory implements EventBuilder.Factory {

        public static final Factory INSTANCE = new Factory();

        private Factory() {
        }

        @Override
        public FirebaseEventBuilder createEventBuilder() {
            return new FirebaseEventBuilder();
        }
    }
}
