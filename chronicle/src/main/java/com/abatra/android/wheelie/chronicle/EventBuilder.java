package com.abatra.android.wheelie.chronicle;

import android.os.Parcelable;

import com.abatra.android.wheelie.chronicle.model.BeginCheckoutEventParams;
import com.abatra.android.wheelie.chronicle.model.PurchaseEventParams;
import com.abatra.android.wheelie.chronicle.model.ScreenViewEventParams;
import com.abatra.android.wheelie.chronicle.model.SelectItemEventParams;

public abstract class EventBuilder<T extends EventBuilder<T>> {

    protected abstract EventParams createEventParams();

    public abstract Event buildBeginCheckoutEvent(BeginCheckoutEventParams beginCheckoutEventParams);

    public abstract Event buildPurchaseEvent(PurchaseEventParams purchaseEventParams);

    public abstract Event buildScreenViewEvent(ScreenViewEventParams screenViewEventParams);

    private String eventName;
    private final EventParams eventParams = createEventParams();

    @SuppressWarnings("UnusedReturnValue")
    public T withName(String eventName) {
        this.eventName = eventName;
        //noinspection unchecked
        return (T) this;
    }

    @SuppressWarnings("UnusedReturnValue")
    public T withParam(String key, String value) {
        eventParams.put(key, value);
        //noinspection unchecked
        return (T) this;
    }

    public T withParam(String key, long value) {
        eventParams.put(key, value);
        //noinspection unchecked
        return (T) this;
    }

    @SuppressWarnings("UnusedReturnValue")
    public T withParam(String key, double value) {
        eventParams.put(key, value);
        //noinspection unchecked
        return (T) this;
    }

    @SuppressWarnings("UnusedReturnValue")
    public T withParam(String key, Parcelable[] value) {
        eventParams.put(key, value);
        //noinspection unchecked
        return (T) this;
    }

    public T withParam(String key, boolean value) {
        eventParams.put(key, value);
        //noinspection unchecked
        return (T) this;
    }

    public Event buildEventOfName(String eventName) {
        return withName(eventName)
                .build();
    }

    public Event build() throws IllegalArgumentException {
        return new Event(eventName, eventParams);
    }

    public void record() {
        Chronicle.record(build());
    }

    public abstract Event buildSelectItemEvent(SelectItemEventParams selectItemEventParams);

    public interface Factory {
        EventBuilder<?> createEventBuilder();
    }

}
