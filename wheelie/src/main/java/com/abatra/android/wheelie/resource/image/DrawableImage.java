package com.abatra.android.wheelie.resource.image;

import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.appcompat.widget.AppCompatImageView;

class DrawableImage implements Image {

    private final Drawable drawable;

    DrawableImage(Drawable drawable) {
        this.drawable = drawable;
    }

    @Override
    public void setTo(AppCompatImageView appCompatImageView) {
        appCompatImageView.setImageDrawable(drawable);
    }

    @Override
    public Drawable getDrawable(Context context) {
        return drawable;
    }
}
