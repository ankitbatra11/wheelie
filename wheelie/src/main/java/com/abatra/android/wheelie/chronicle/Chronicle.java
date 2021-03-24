package com.abatra.android.wheelie.chronicle;

import android.content.Context;

import androidx.fragment.app.Fragment;

import com.abatra.android.wheelie.chronicle.model.BeginCheckoutEventParams;
import com.abatra.android.wheelie.chronicle.model.PurchaseEventParams;
import com.abatra.android.wheelie.chronicle.model.SelectItemEventParams;

public class Chronicle {

    private static ChronicleConfig config;

    private Chronicle() {
    }

    public static void initializeForFirebase(Context context) {
        setConfig(ChronicleConfig.forFirebase(context));
    }

    static void setConfig(ChronicleConfig config) {
        Chronicle.config = config;
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

    static EventRecorder eventRecorder() {
        return config.getEventRecorder();
    }

    public static void recordBeginCheckoutEvent(BeginCheckoutEventParams beginCheckoutEventParams) {
        record(eventBuilder().buildBeginCheckoutEvent(beginCheckoutEventParams));
    }

    public static void recordPurchaseEvent(PurchaseEventParams purchaseEventParams) {
        record(eventBuilder().buildPurchaseEvent(purchaseEventParams));
    }

    public static void recordSelectItemEvent(SelectItemEventParams selectItemEventParams) {
        record(eventBuilder().buildSelectItemEvent(selectItemEventParams));
    }

    public static void setUserProperty(String propertyName, String propertyValue) {
        config.getUserPropertySetter().setUserProperty(propertyName, propertyValue);
    }
}
