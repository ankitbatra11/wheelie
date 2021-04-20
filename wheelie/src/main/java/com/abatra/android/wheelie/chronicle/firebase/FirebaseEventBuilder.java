package com.abatra.android.wheelie.chronicle.firebase;

import android.os.Bundle;
import android.os.Parcelable;

import androidx.annotation.Nullable;

import com.abatra.android.wheelie.chronicle.Event;
import com.abatra.android.wheelie.chronicle.EventBuilder;
import com.abatra.android.wheelie.chronicle.EventParams;
import com.abatra.android.wheelie.chronicle.model.BeginCheckoutEventParams;
import com.abatra.android.wheelie.chronicle.model.Item;
import com.abatra.android.wheelie.chronicle.model.Price;
import com.abatra.android.wheelie.chronicle.model.PurchasableItem;
import com.abatra.android.wheelie.chronicle.model.PurchaseEventParams;
import com.abatra.android.wheelie.chronicle.model.ScreenViewEventParams;
import com.abatra.android.wheelie.chronicle.model.SelectItemEventParams;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import timber.log.Timber;

import static com.google.firebase.analytics.FirebaseAnalytics.Event.BEGIN_CHECKOUT;
import static com.google.firebase.analytics.FirebaseAnalytics.Event.PURCHASE;
import static com.google.firebase.analytics.FirebaseAnalytics.Event.SCREEN_VIEW;
import static com.google.firebase.analytics.FirebaseAnalytics.Event.SELECT_ITEM;
import static com.google.firebase.analytics.FirebaseAnalytics.Param.AFFILIATION;
import static com.google.firebase.analytics.FirebaseAnalytics.Param.COUPON;
import static com.google.firebase.analytics.FirebaseAnalytics.Param.CURRENCY;
import static com.google.firebase.analytics.FirebaseAnalytics.Param.ITEMS;
import static com.google.firebase.analytics.FirebaseAnalytics.Param.ITEM_BRAND;
import static com.google.firebase.analytics.FirebaseAnalytics.Param.ITEM_CATEGORY;
import static com.google.firebase.analytics.FirebaseAnalytics.Param.ITEM_ID;
import static com.google.firebase.analytics.FirebaseAnalytics.Param.ITEM_LIST_ID;
import static com.google.firebase.analytics.FirebaseAnalytics.Param.ITEM_LIST_NAME;
import static com.google.firebase.analytics.FirebaseAnalytics.Param.ITEM_NAME;
import static com.google.firebase.analytics.FirebaseAnalytics.Param.ITEM_VARIANT;
import static com.google.firebase.analytics.FirebaseAnalytics.Param.PRICE;
import static com.google.firebase.analytics.FirebaseAnalytics.Param.QUANTITY;
import static com.google.firebase.analytics.FirebaseAnalytics.Param.SCREEN_CLASS;
import static com.google.firebase.analytics.FirebaseAnalytics.Param.SCREEN_NAME;
import static com.google.firebase.analytics.FirebaseAnalytics.Param.SHIPPING;
import static com.google.firebase.analytics.FirebaseAnalytics.Param.TAX;
import static com.google.firebase.analytics.FirebaseAnalytics.Param.TRANSACTION_ID;
import static com.google.firebase.analytics.FirebaseAnalytics.Param.VALUE;

public class FirebaseEventBuilder extends EventBuilder<FirebaseEventBuilder> {

    @Override
    protected EventParams createEventParams() {
        return EventParams.bundled();
    }

    @Override
    public Event buildBeginCheckoutEvent(BeginCheckoutEventParams beginCheckoutEventParams) {
        return withName(BEGIN_CHECKOUT)
                .withParam(COUPON, beginCheckoutEventParams.getCoupon())
                .withPriceParam(beginCheckoutEventParams.getPrice())
                .withParam(ITEMS, purchasableItemsToParcelables(beginCheckoutEventParams.getItems()))
                .build();
    }

    private EventBuilder<?> withPriceParam(@Nullable Price price) {
        Optional.ofNullable(price).ifPresent(p -> {
            if (p.getCurrency() != null) {
                withParam(CURRENCY, p.getCurrency());
                withParam(VALUE, p.getValue());
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
        bundle.putString(ITEM_ID, item.getId());
        bundle.putString(ITEM_NAME, item.getName());
        bundle.putString(ITEM_CATEGORY, item.getCategory());
        bundle.putString(ITEM_BRAND, item.getBrand());
        bundle.putString(ITEM_VARIANT, item.getVariant());
        bundle.putDouble(PRICE, item.getPrice());
        bundle.putLong(QUANTITY, item.getQuantity());
        return bundle;
    }

    @Override
    public Event buildPurchaseEvent(PurchaseEventParams purchaseEventParams) {
        return withName(PURCHASE)
                .withParam(TRANSACTION_ID, purchaseEventParams.getTransactionId())
                .withParam(AFFILIATION, purchaseEventParams.getAffiliation())
                .withPriceParam(purchaseEventParams.getPrice())
                .withParam(TAX, purchaseEventParams.getTax())
                .withParam(SHIPPING, purchaseEventParams.getShipping())
                .withParam(COUPON, purchaseEventParams.getCoupon())
                .withParam(ITEMS, purchasableItemsToParcelables(purchaseEventParams.getItems()))
                .build();
    }

    @Override
    public Event buildSelectItemEvent(SelectItemEventParams selectItemEventParams) {

        withName(SELECT_ITEM)
                .withParam(ITEM_LIST_ID, selectItemEventParams.getItemListId())
                .withParam(ITEM_LIST_NAME, selectItemEventParams.getItemListName());

        Optional.ofNullable(selectItemEventParams.getItem()).ifPresent(item -> {
            List<Item> items = Collections.singletonList(item);
            withParam(ITEMS, itemsToParcelables(items));
        });
        return build();
    }

    private Parcelable[] itemsToParcelables(List<Item> items) {
        return items.stream()
                .map(this::createBundle)
                .toArray(Parcelable[]::new);
    }

    private Bundle createBundle(Item item) {
        Bundle bundle = new Bundle();
        bundle.putString(ITEM_ID, item.getId());
        bundle.putString(ITEM_NAME, item.getName());
        return bundle;
    }

    @Override
    public Event buildScreenViewEvent(ScreenViewEventParams screenViewEventParams) {
        return withName(SCREEN_VIEW)
                .withParam(SCREEN_CLASS, screenViewEventParams.getScreenClass())
                .withParam(SCREEN_NAME, screenViewEventParams.getScreenName())
                .build();
    }

    public static class Factory implements EventBuilder.Factory {

        @Override
        public FirebaseEventBuilder createEventBuilder() {
            return new FirebaseEventBuilder();
        }
    }
}
