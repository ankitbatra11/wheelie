package com.abatra.android.wheelie.startup;

import android.app.Application;

/**
 * Initializer for component that depends on {@link Application}.
 */
public interface ApplicationInitializer {
    /**
     * Initializes component.
     *
     * @param application to use to initialize the component.
     */
    void initialize(Application application);
}
