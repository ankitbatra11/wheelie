package com.abatra.android.wheelie.core.res.text;

import android.content.Context;
import android.widget.TextView;

import androidx.annotation.NonNull;

class StringText implements Text {

    private final String text;

    StringText(String text) {
        this.text = text;
    }

    @Override
    public void show(TextView textView) {
        textView.setText(text);
    }

    @Override
    public String getString(Context context) {
        return text;
    }

    @NonNull
    @Override
    public String toString() {
        return "StringText{" +
                "text='" + text + '\'' +
                '}';
    }
}
