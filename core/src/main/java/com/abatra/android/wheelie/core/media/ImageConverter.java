package com.abatra.android.wheelie.core.media;

import android.graphics.Bitmap;
import android.media.Image;
import android.os.Build;
import android.util.Size;

import androidx.annotation.RequiresApi;

import java.nio.ByteBuffer;

import timber.log.Timber;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class ImageConverter {

    public Bitmap toBitmap(Image image, Size size) {
        Image.Plane[] planes = getPlanesOrThrow(image);
        return createBitmap(image, size, planes);
    }

    private Bitmap createBitmap(Image image, Size size, Image.Plane[] planes) {
        ByteBuffer buffer = getByteBufferOrThrow(image, planes);
        int pixelStride = planes[0].getPixelStride();
        int rowStride = planes[0].getRowStride();
        int rowPadding = rowStride - pixelStride * size.getWidth();
        Bitmap bitmap = createBitmapWithAvailableMemory(size, rowPadding, pixelStride);
        buffer.rewind();
        bitmap.copyPixelsFromBuffer(buffer);
        buffer.clear();
        return bitmap;
    }

    private ByteBuffer getByteBufferOrThrow(Image image, Image.Plane[] planes) {
        ByteBuffer buffer = planes[0].getBuffer();
        if (buffer == null) {
            throw new RuntimeException(String.format("image=%s planes[0]=%s buffer is null", image, planes[0]));
        }
        return buffer;
    }

    private Image.Plane[] getPlanesOrThrow(Image image) {
        Image.Plane[] planes = image.getPlanes();
        if (planes == null || planes.length == 0) {
            throw new RuntimeException(String.format("image=%s does not have planes", image));
        }
        return planes;
    }

    private Bitmap createBitmapWithAvailableMemory(Size size, int rowPadding, int pixelStride) {
        Bitmap bitmap;
        int width = size.getWidth() + rowPadding / pixelStride;
        int height = size.getHeight();
        try {
            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        } catch (Throwable e) {
            Timber.e(e, "Could not create bitmap with ARGB_8888 config");
            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        }
        return bitmap;
    }
}
