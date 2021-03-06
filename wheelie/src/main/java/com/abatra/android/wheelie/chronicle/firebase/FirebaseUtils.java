package com.abatra.android.wheelie.chronicle.firebase;

import androidx.annotation.Nullable;

import java.util.Optional;

public class FirebaseUtils {

    private FirebaseUtils() {
    }

    @Nullable
    public static Double getValue(@Nullable FirebasePrice checkoutPrice) {
        return Optional.ofNullable(checkoutPrice)
                .map(FirebasePrice::getValue)
                .orElse(null);
    }

    @Nullable
    public static String getCurrency(@Nullable FirebasePrice checkoutPrice) {
        return Optional.ofNullable(checkoutPrice)
                .map(FirebasePrice::getCurrency)
                .orElse(null);
    }
}
