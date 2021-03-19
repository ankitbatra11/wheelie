package com.abatra.android.wheelie.chronicle.firebase;

import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;

import androidx.fragment.app.Fragment;

import com.abatra.android.wheelie.chronicle.BundleEventParams;
import com.abatra.android.wheelie.chronicle.Event;
import com.abatra.android.wheelie.chronicle.model.BeginCheckoutEventParams;
import com.abatra.android.wheelie.chronicle.model.Item;
import com.abatra.android.wheelie.chronicle.model.PurchasableItem;
import com.abatra.android.wheelie.chronicle.model.Price;
import com.abatra.android.wheelie.chronicle.model.PurchaseEventParams;
import com.abatra.android.wheelie.chronicle.model.SelectItemEventParams;
import com.google.common.collect.ImmutableMap;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.firebase.analytics.FirebaseAnalytics.Event.BEGIN_CHECKOUT;
import static com.google.firebase.analytics.FirebaseAnalytics.Event.PURCHASE;
import static com.google.firebase.analytics.FirebaseAnalytics.Event.SCREEN_VIEW;
import static com.google.firebase.analytics.FirebaseAnalytics.Param;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.instanceOf;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = Build.VERSION_CODES.P)
public class FirebaseEventBuilderTest {

    private FirebaseEventBuilder firebaseEventBuilder;

    @Mock
    private Fragment mockedFragment;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
        firebaseEventBuilder = FirebaseEventBuilder.Factory.INSTANCE.createEventBuilder();
    }

    @Test
    public void test_createEventParams() {
        assertThat(firebaseEventBuilder.createEventParams(), instanceOf(BundleEventParams.class));
    }

    @Test
    public void test_buildScreenViewEvent() {

        Event event = firebaseEventBuilder.buildScreenViewEvent(mockedFragment);

        verifyEvent(event, SCREEN_VIEW, ImmutableMap.of(
                Param.SCREEN_CLASS, mockedFragment.getClass().getSimpleName(),
                Param.SCREEN_NAME, mockedFragment.getClass().getSimpleName()));
    }

    private void verifyEvent(Event event, String expectedName, Map<String, Object> expectedParams) {
        assertThat(event.getName(), equalTo(expectedName));
        verifyParams(event, expectedParams);
    }

    private void verifyParams(Event event, Map<String, Object> expectedParams) {
        BundleEventParams bundleEventParams = requireBundleEventParams(event);
        assertThat(bundleEventParams.bundle().size(), greaterThanOrEqualTo(expectedParams.size()));
        for (Map.Entry<String, Object> expectedParam : expectedParams.entrySet()) {
            assertThat(bundleEventParams.bundle().get(expectedParam.getKey()), equalTo(expectedParam.getValue()));
        }
    }

    private BundleEventParams requireBundleEventParams(Event event) {
        assertThat(event.getEventParams(), instanceOf(BundleEventParams.class));
        return (BundleEventParams) event.getEventParams();
    }

    @Test
    public void test_buildBeginCheckoutEvent() {

        PurchasableItem purchasableItem = createPurchasableItem();
        BeginCheckoutEventParams params = new BeginCheckoutEventParams()
                .setCoupon("coupon")
                .setPrice(new Price(purchasableItem.getPrice(), "INR"))
                .addItem(purchasableItem);

        Event event = firebaseEventBuilder.buildBeginCheckoutEvent(params);

        verifyParamCount(event, 4);

        verifyEvent(event, BEGIN_CHECKOUT, ImmutableMap.of(
                Param.COUPON, params.getCoupon(),
                Param.VALUE, params.getPrice().getValue(),
                Param.CURRENCY, params.getPrice().getCurrency()));

        verifyPurchasableItem(event, purchasableItem);
    }

    private PurchasableItem createPurchasableItem() {
        return new PurchasableItem()
                .setBrand("brand")
                .setCategory("category")
                .setId("id")
                .setName("name")
                .setPrice(1)
                .setQuantity(2)
                .setVariant("black");
    }

    private void verifyParamCount(Event event, int expectedCount) {
        assertThat(requireBundleEventParams(event).bundle().size(), equalTo(expectedCount));
    }

    private void verifyPurchasableItem(Event event, PurchasableItem purchasableItem) {
        Parcelable[] parcelables = requireBundleEventParams(event).bundle().getParcelableArray(Param.ITEMS);
        assertThat(parcelables.length, equalTo(1));
        assertThat(parcelables[0], instanceOf(Bundle.class));
        Bundle bundle = (Bundle) parcelables[0];
        assertThat(bundle.size(), equalTo(7));
        assertThat(bundle.get(Param.ITEM_BRAND), equalTo(purchasableItem.getBrand()));
        assertThat(bundle.get(Param.ITEM_CATEGORY), equalTo(purchasableItem.getCategory()));
        assertThat(bundle.get(Param.ITEM_ID), equalTo(purchasableItem.getId()));
        assertThat(bundle.get(Param.ITEM_NAME), equalTo(purchasableItem.getName()));
        assertThat(bundle.get(Param.ITEM_VARIANT), equalTo(purchasableItem.getVariant()));
        assertThat(bundle.get(Param.PRICE), equalTo(purchasableItem.getPrice()));
        assertThat(bundle.get(Param.QUANTITY), equalTo(purchasableItem.getQuantity()));
    }

    @Test
    public void test_buildPurchaseEvent() {

        PurchasableItem purchasableItem = createPurchasableItem();
        PurchaseEventParams params = new PurchaseEventParams()
                .setAffiliation("play store")
                .setShipping(3)
                .setTax(5)
                .setTransactionId("txn id")
                .setCoupon("coupon")
                .setPrice(new Price(purchasableItem.getPrice(), "INR"))
                .addPurchasedItem(purchasableItem);

        Event event = firebaseEventBuilder.buildPurchaseEvent(params);

        verifyParamCount(event, 8);

        Map<String, Object> expectedParams = new HashMap<>();
        expectedParams.put(Param.AFFILIATION, params.getAffiliation());
        expectedParams.put(Param.SHIPPING, params.getShipping());
        expectedParams.put(Param.TAX, params.getTax());
        expectedParams.put(Param.TRANSACTION_ID, params.getTransactionId());
        expectedParams.put(Param.COUPON, params.getCoupon());
        expectedParams.put(Param.VALUE, params.getPrice().getValue());
        expectedParams.put(Param.CURRENCY, params.getPrice().getCurrency());
        verifyEvent(event, PURCHASE, expectedParams);

        verifyPurchasableItem(event, purchasableItem);
    }

    @Test
    public void test_buildSelectItemEvent() {

        String expectedItemListId = "itemListId";
        String expectedItemListName = "itemListName";

        List<Item> items = Arrays.asList(createItem(1), createItem(2));
        SelectItemEventParams params = new SelectItemEventParams()
                .setItemListId(expectedItemListId)
                .setItemListName(expectedItemListName)
                .setItems(items);

        Event event = firebaseEventBuilder.buildSelectItemEvent(params);

        verifyEvent(event, FirebaseAnalytics.Event.SELECT_ITEM, ImmutableMap.of(
                Param.ITEM_LIST_ID, expectedItemListId,
                Param.ITEM_LIST_NAME, expectedItemListName));

        verifyItems(event, items);
    }

    private void verifyItems(Event event, List<Item> items) {
        Parcelable[] parcelables = requireBundleEventParams(event).bundle().getParcelableArray(Param.ITEMS);
        assertThat(parcelables.length, equalTo(items.size()));
        for (int i = 0; i < parcelables.length; i++) {
            Bundle bundle = (Bundle) parcelables[i];
            assertThat(bundle.get(Param.ITEM_ID), equalTo(items.get(i).getId()));
            assertThat(bundle.get(Param.ITEM_NAME), equalTo(items.get(i).getName()));
        }
    }

    private Item createItem(int id) {
        return new Item().setId("id" + id).setName("name" + id);
    }
}
