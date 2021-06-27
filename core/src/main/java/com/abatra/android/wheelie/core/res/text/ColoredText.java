package com.abatra.android.wheelie.core.res.text;

import android.content.Context;
import android.widget.TextView;

import androidx.annotation.NonNull;

class ColoredText implements Text {

    private final Text text;
    private final int color;

    ColoredText(Text text, int color) {
        this.text = text;
        this.color = color;
    }

    @Override
    public void show(TextView textView) {
        text.show(textView);
        textView.setTextColor(color);
    }

    @Override
    public String getString(Context context) {
        return text.getString(context);
    }

    @NonNull
    @Override
    public String toString() {
        return "ColoredText{" +
                "text=" + text +
                ", color=" + color +
                '}';
    }
}
