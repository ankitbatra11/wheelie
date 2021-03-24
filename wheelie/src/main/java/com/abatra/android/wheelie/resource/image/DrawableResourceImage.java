package com.abatra.android.wheelie.resource.image;

import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.annotation.DrawableRes;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatImageView;

class DrawableResourceImage implements Image {

    @DrawableRes
    private final int res;

    DrawableResourceImage(int res) {
        this.res = res;
    }

    @Override
    public void setTo(AppCompatImageView appCompatImageView) {
        appCompatImageView.setImageResource(res);
    }

    @Override
    public Drawable getDrawable(Context context) {
        return AppCompatResources.getDrawable(context, res);
    }
}
