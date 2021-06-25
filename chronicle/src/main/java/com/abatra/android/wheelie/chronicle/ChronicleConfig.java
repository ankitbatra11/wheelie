package com.abatra.android.wheelie.chronicle;

public class ChronicleConfig {

    private final EventBuilder.Factory factory;
    private final EventRecorder eventRecorder;
    private final UserPropertySetter userPropertySetter;

    public ChronicleConfig(EventBuilder.Factory factory,
                           EventRecorder eventRecorder,
                           UserPropertySetter userPropertySetter) {
        this.factory = factory;
        this.eventRecorder = eventRecorder;
        this.userPropertySetter = userPropertySetter;
    }

    EventBuilder.Factory getEventBuilderFactory() {
        return factory;
    }

    EventRecorder getEventRecorder() {
        return eventRecorder;
    }

    UserPropertySetter getUserPropertySetter() {
        return userPropertySetter;
    }
}
