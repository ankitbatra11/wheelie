package com.abatra.android.wheelie.core.res.image;

import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.annotation.DrawableRes;
import androidx.appcompat.widget.AppCompatImageView;

public interface Image {

    Image NULL = new NullImage();

    static Image drawableRes(@DrawableRes int res) {
        return new DrawableResourceImage(res);
    }

    static Image drawable(Drawable drawable) {
        return new DrawableImage(drawable);
    }

    static Image tint(Image image, int color) {
        return new TintedImage(image, color);
    }

    static Image tint(@DrawableRes int res, int color) {
        return tint(drawableRes(res), color);
    }

    void setTo(AppCompatImageView appCompatImageView);

    Drawable getDrawable(Context context);

    default Image tint(int color) {
        return new TintedImage(this, color);
    }
}
