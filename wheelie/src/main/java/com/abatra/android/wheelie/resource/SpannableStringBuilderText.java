package com.abatra.android.wheelie.resource;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.widget.TextView;

public class SpannableStringBuilderText implements Text {

    private final SpannableStringBuilder spannableStringBuilder;

    public SpannableStringBuilderText(SpannableStringBuilder spannableStringBuilder) {
        this.spannableStringBuilder = spannableStringBuilder;
    }

    @Override
    public void show(TextView textView) {
        textView.setText(spannableStringBuilder);
    }

    @Override
    public String getString(Context context) {
        return spannableStringBuilder.toString();
    }
}
