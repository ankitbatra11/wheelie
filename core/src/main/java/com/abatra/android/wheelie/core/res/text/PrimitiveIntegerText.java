package com.abatra.android.wheelie.core.res.text;

import android.content.Context;
import android.widget.TextView;

import androidx.annotation.NonNull;

class PrimitiveIntegerText implements Text {

    private final int integer;

    PrimitiveIntegerText(int integer) {
        this.integer = integer;
    }

    @Override
    public void show(TextView textView) {
        textView.setText(getString(textView.getContext()));
    }

    @Override
    public String getString(Context context) {
        return String.valueOf(integer);
    }

    @NonNull
    @Override
    public String toString() {
        return "PrimitiveIntegerText{" +
                "integer=" + integer +
                '}';
    }
}
