package com.abatra.android.wheelie.chronicle.firebase;

public interface FirebasePriceParam<T extends FirebasePriceParam<T>> {

    T setFirebasePrice(FirebasePrice firebasePrice);

    FirebasePrice getFirebasePrice();

    default Double getValue() {
        return FirebaseUtils.getValue(getFirebasePrice());
    }

    default String getCurrency() {
        return FirebaseUtils.getCurrency(getFirebasePrice());
    }
}
