package com.abatra.android.wheelie.core.res.text;

import android.content.Context;
import android.widget.TextView;

class NullText implements Text {

    @Override
    public void show(TextView textView) {
        textView.setText(null);
    }

    @Override
    public String getString(Context context) {
        return null;
    }
}
