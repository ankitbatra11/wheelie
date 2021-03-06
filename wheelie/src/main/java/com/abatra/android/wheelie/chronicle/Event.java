package com.abatra.android.wheelie.chronicle;

public class Event {

    private final String name;
    private final EventParams eventParams;

    Event(String name, EventParams eventParams) {
        this.name = name;
        this.eventParams = eventParams;
    }

    public String getName() {
        return name;
    }

    public EventParams getEventParams() {
        return eventParams;
    }
}
