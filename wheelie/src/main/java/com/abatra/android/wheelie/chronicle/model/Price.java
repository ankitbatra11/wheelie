package com.abatra.android.wheelie.chronicle.model;

import androidx.annotation.NonNull;

public class Price {

    private final double value;
    private final String currency;

    public Price(double value, String currency) {
        this.value = value;
        this.currency = currency;
    }

    public double getValue() {
        return value;
    }

    public String getCurrency() {
        return currency;
    }

    @NonNull
    @Override
    public String toString() {
        return "Price{" +
                "value=" + value +
                ", currency='" + currency + '\'' +
                '}';
    }
}
