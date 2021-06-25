package com.abatra.android.wheelie.core.res.image;

import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.graphics.drawable.DrawableCompat;

class TintedImage implements Image {

    private final Image image;
    private final int color;

    TintedImage(Image image, int color) {
        this.image = image;
        this.color = color;
    }

    @Override
    public void setTo(AppCompatImageView appCompatImageView) {
        appCompatImageView.setImageDrawable(getDrawable(appCompatImageView.getContext()));
    }

    @Override
    public Drawable getDrawable(Context context) {
        Drawable drawable = DrawableCompat.wrap(image.getDrawable(context));
        DrawableCompat.setTint(drawable, color);
        return drawable;
    }
}
