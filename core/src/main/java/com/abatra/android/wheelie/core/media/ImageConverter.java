package com.abatra.android.wheelie.core.media;

import android.graphics.Bitmap;
import android.media.Image;

public interface ImageConverter {
    byte[] toByteArray(Image image);

    void toBitmap(Image image, Bitmap outputBitmap);
}
