package com.abatra.android.wheelie.resource;

import android.content.Context;
import android.widget.TextView;

import androidx.annotation.StringRes;

public interface Text {

    Text NULL = new NullText();

    static Text stringResource(@StringRes int res) {
        return new StringResourceText(res);
    }

    static Text string(String text) {
        return new StringText(text);
    }

    static Text primitiveInteger(int integer) {
        return new PrimitiveIntegerText(integer);
    }

    void show(TextView textView);

    String getString(Context context);

    default Text colored(int color) {
        return new ColoredText(this, color);
    }
}
