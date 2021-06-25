package com.abatra.android.wheelie.core.res.text;

import android.content.Context;
import android.widget.TextView;

import androidx.annotation.StringRes;

class StringResourceText implements Text {

    @StringRes
    private final int res;

    StringResourceText(int res) {
        this.res = res;
    }

    @Override
    public void show(TextView textView) {
        textView.setText(res);
    }

    @Override
    public String getString(Context context) {
        return context.getString(res);
    }
}
