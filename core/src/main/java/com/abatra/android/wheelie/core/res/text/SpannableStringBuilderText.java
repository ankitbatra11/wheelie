package com.abatra.android.wheelie.core.res.text;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.widget.TextView;

import androidx.annotation.NonNull;

class SpannableStringBuilderText implements Text {

    private final SpannableStringBuilder spannableStringBuilder;

    SpannableStringBuilderText(SpannableStringBuilder spannableStringBuilder) {
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

    @NonNull
    @Override
    public String toString() {
        return "SpannableStringBuilderText{" +
                "spannableStringBuilder=" + spannableStringBuilder +
                '}';
    }
}
