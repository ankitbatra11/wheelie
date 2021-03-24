package com.abatra.android.wheelie.chronicle.firebase;

import com.abatra.android.wheelie.chronicle.UserPropertySetter;
import com.google.firebase.analytics.FirebaseAnalytics;

public class FirebaseUserPropertySetter implements UserPropertySetter {

    private final FirebaseAnalytics firebaseAnalytics;

    public FirebaseUserPropertySetter(FirebaseAnalytics firebaseAnalytics) {
        this.firebaseAnalytics = firebaseAnalytics;
    }

    @Override
    public void setUserProperty(String propertyName, String propertyValue) {
        firebaseAnalytics.setUserProperty(propertyName, propertyValue);
    }
}
