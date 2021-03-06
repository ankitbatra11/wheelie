package com.abatra.android.wheelie.chronicle.firebase;

public class FirebasePrice {

    private final double value;
    private final String currency;

    public FirebasePrice(double value, String currency) {
        this.value = value;
        this.currency = currency;
    }

    double getValue() {
        return value;
    }

    String getCurrency() {
        return currency;
    }

    @Override
    public String toString() {
        return "FirebasePrice{" +
                "value=" + value +
                ", currency='" + currency + '\'' +
                '}';
    }
}
