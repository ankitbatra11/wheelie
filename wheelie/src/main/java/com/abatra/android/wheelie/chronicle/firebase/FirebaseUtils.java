package com.abatra.android.wheelie.chronicle.firebase;

import androidx.annotation.Nullable;

import com.abatra.android.wheelie.chronicle.model.Price;

import java.util.Optional;

public class FirebaseUtils {

    private FirebaseUtils() {
    }

    @Nullable
    public static Double getValue(@Nullable Price checkoutPrice) {
        return Optional.ofNullable(checkoutPrice)
                .map(Price::getValue)
                .orElse(null);
    }

    @Nullable
    public static String getCurrency(@Nullable Price checkoutPrice) {
        return Optional.ofNullable(checkoutPrice)
                .map(Price::getCurrency)
                .orElse(null);
    }
}
