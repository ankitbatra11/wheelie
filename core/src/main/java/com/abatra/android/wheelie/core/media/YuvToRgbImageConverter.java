package com.abatra.android.wheelie.core.media;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.media.Image;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicYuvToRGB;
import android.renderscript.Type;

import androidx.annotation.RequiresApi;

import java.nio.ByteBuffer;

import static com.google.common.base.Preconditions.*;

/**
 * https://github.com/android/camera-samples/blob/2690075bca44d51e4e37df07273d1f8d7fe9846f/Camera2SlowMotion/utils/src/main/java/com/example/android/camera/utils/YuvToRgbConverter.kt
 */
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class YuvToRgbImageConverter implements ImageConverter {

    private final RenderScript renderScript;
    private final ScriptIntrinsicYuvToRGB scriptIntrinsicYuvToRGB;

    public YuvToRgbImageConverter(RenderScript renderScript, ScriptIntrinsicYuvToRGB scriptIntrinsicYuvToRGB) {
        this.renderScript = renderScript;
        this.scriptIntrinsicYuvToRGB = scriptIntrinsicYuvToRGB;
    }

    public YuvToRgbImageConverter newInstance(Context context) {
        RenderScript renderScript = RenderScript.create(context);
        ScriptIntrinsicYuvToRGB yuvToRGB = ScriptIntrinsicYuvToRGB.create(renderScript, Element.U8_4(renderScript));
        return new YuvToRgbImageConverter(renderScript, yuvToRGB);
    }

    @Override
    public void toBitmap(Image image, Bitmap outputBitmap) {
        // Get the YUV data in byte array form using NV21 format
        byte[] yuvBuffer = toByteArray(image);

        // Explicitly create an element with type NV21, since that's the pixel format we use
        Type elemType = new Type.Builder(renderScript, Element.YUV(renderScript))
                .setYuvFormat(ImageFormat.NV21)
                .create();

        Allocation inputAllocation = Allocation.createSized(renderScript, elemType.getElement(), yuvBuffer.length);
        Allocation outputAllocation = Allocation.createFromBitmap(renderScript, outputBitmap);

        // Convert NV21 format YUV to RGB
        inputAllocation.copyFrom(yuvBuffer);
        scriptIntrinsicYuvToRGB.setInput(inputAllocation);
        scriptIntrinsicYuvToRGB.forEach(outputAllocation);
        outputAllocation.copyTo(outputBitmap);
    }

    @Override
    public byte[] toByteArray(Image image) {
        checkArgument(image.getFormat() == ImageFormat.YUV_420_888, "Unsupported image format=%s", image.getFormat());

        // Ensure that the intermediate output byte buffer is allocated
        int pixelCount = image.getCropRect().width() * image.getCropRect().height();
        // Bits per pixel is an average for the whole image, so it's useful to compute the size
        // of the full buffer but should not be used to determine pixel offsets
        int pixelSizeBits = ImageFormat.getBitsPerPixel(ImageFormat.YUV_420_888);
        byte[] outputBuffer = new byte[pixelCount * pixelSizeBits / 8];

        Rect imageCrop = image.getCropRect();
        Image.Plane[] imagePlanes = image.getPlanes();

        for (int planeIndex = 0; planeIndex < imagePlanes.length; planeIndex++) {
            Image.Plane plane = imagePlanes[planeIndex];
            int outputStride;
            int outputOffset;
            switch (planeIndex) {
                case 0:
                    outputStride = 1;
                    outputOffset = 0;
                    break;
                case 1:
                    outputStride = 2;
                    // For NV21 format, U is in odd-numbered indices
                    outputOffset = pixelCount + 1;
                    break;
                case 2:
                    outputStride = 2;
                    // For NV21 format, V is in even-numbered indices
                    outputOffset = pixelCount;
                    break;
                default:
                    continue;
            }
            ByteBuffer planeBuffer = plane.getBuffer();
            int rowStride = plane.getRowStride();
            int pixelStride = plane.getPixelStride();

            // We have to divide the width and height by two if it's not the Y plane
            Rect planeCrop = planeIndex == 0 ? imageCrop : new Rect(
                    imageCrop.left / 2,
                    imageCrop.top / 2,
                    imageCrop.right / 2,
                    imageCrop.bottom / 2
            );

            int planeWidth = planeCrop.width();
            int planeHeight = planeCrop.height();

            // Intermediate buffer used to store the bytes of each row
            byte[] rowBuffer = new byte[plane.getRowStride()];

            // Size of each row in bytes
            int rowLength;
            if (pixelStride == 1 && outputStride == 1) {
                rowLength = planeWidth;
            } else {
                // Take into account that the stride may include data from pixels other than this
                // particular plane and row, and that could be between pixels and not after every
                // pixel:
                //
                // |---- Pixel stride ----|                    Row ends here --> |
                // | Pixel 1 | Other Data | Pixel 2 | Other Data | ... | Pixel N |
                //
                // We need to get (N-1) * (pixel stride bytes) per row + 1 byte for the last pixel
                rowLength = (planeWidth - 1) * pixelStride + 1;
            }

            for (int row = 0; row < planeHeight; row++) {
                // Move buffer position to the beginning of this row
                planeBuffer.position((row + planeCrop.top) * rowStride + planeCrop.left * pixelStride);
                if (pixelStride == 1 && outputStride == 1) {
                    // When there is a single stride value for pixel and output, we can just copy
                    // the entire row in a single step
                    planeBuffer.get(outputBuffer, outputOffset, rowLength);
                    outputOffset += rowLength;
                } else {
                    // When either pixel or output have a stride > 1 we must copy pixel by pixel
                    planeBuffer.get(rowBuffer, 0, rowLength);
                    for (int col = 0; col < planeWidth; col++) {
                        outputBuffer[outputOffset] = rowBuffer[col * pixelStride];
                        outputOffset += outputStride;
                    }
                }
            }
        }

        return outputBuffer;
    }
}
