package com.abatra.android.wheelie.core.res.color;

import androidx.annotation.ColorRes;

public class ResourceColor implements IColor {

    @ColorRes
    private final int colorRes;

    public ResourceColor(int colorRes) {
        this.colorRes = colorRes;
    }
}
