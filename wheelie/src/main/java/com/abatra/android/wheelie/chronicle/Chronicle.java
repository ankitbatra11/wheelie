package com.abatra.android.wheelie.chronicle;

import android.content.Context;

import androidx.fragment.app.Fragment;

import com.abatra.android.wheelie.chronicle.firebase.FirebaseEventBuilder;
import com.abatra.android.wheelie.chronicle.firebase.FirebaseEventRecorder;

public class Chronicle {

    private static ChronicleConfig config;

    private Chronicle() {
    }

    public static void initializeForFirebase(Context context) {
        config = new ChronicleConfig(context,
                FirebaseEventBuilder.Factory.INSTANCE,
                FirebaseEventRecorder.Supplier.INSTANCE);
    }

    public static EventBuilder<?> eventBuilder() {
        return config.getEventBuilderFactory().createEventBuilder();
    }

    public static void recordScreenViewEvent(Fragment fragment) {
        record(eventBuilder().buildScreenViewEvent(fragment));
    }

    public static void recordEventOfName(String eventName) {
        record(eventBuilder().buildEventOfName(eventName));
    }

    public static void record(Event event) {
        eventRecorder().record(event);
    }

    private static EventRecorder eventRecorder() {
        return config.getEventRecorderSupplier().getEventRecorder(config.getContext());
    }

    public static void recordBeginCheckoutEvent(CheckoutEventParams checkoutEventParams) {
        record(eventBuilder().buildBeginCheckoutEvent(checkoutEventParams));
    }

    public static void recordPurchaseEvent(PurchaseEventParams purchaseEventParams) {
        record(eventBuilder().buildPurchaseEvent(purchaseEventParams));
    }
}
