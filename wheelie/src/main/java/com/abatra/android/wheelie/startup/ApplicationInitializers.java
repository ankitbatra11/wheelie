package com.abatra.android.wheelie.startup;

import android.app.Application;

import java.util.Collection;
import java.util.Collections;

import timber.log.Timber;

public class ApplicationInitializers implements ApplicationInitializer {

    public static ApplicationInitializers EMPTY = new ApplicationInitializers(Collections.emptyList());

    private final Collection<ApplicationInitializer> applicationInitializers;

    public ApplicationInitializers(Collection<ApplicationInitializer> applicationInitializers) {
        this.applicationInitializers = applicationInitializers;
    }

    @Override
    public void initialize(Application application) {
        for (ApplicationInitializer applicationInitializer : applicationInitializers) {
            try {
                applicationInitializer.initialize(application);
                Timber.i("Completed applicationInitializer=%s", applicationInitializer);
            } catch (Throwable error) {
                Timber.e(error, "applicationInitializer=%s failed", applicationInitializer);
            }
        }
    }
}
