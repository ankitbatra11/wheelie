package com.abatra.android.wheelie.resource.image;

import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.appcompat.widget.AppCompatImageView;

class NullImage implements Image {

    @Override
    public void setTo(AppCompatImageView appCompatImageView) {
        appCompatImageView.setImageDrawable(null);
    }

    @Override
    public Drawable getDrawable(Context context) {
        return null;
    }
}
