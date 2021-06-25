package com.abatra.android.wheelie.printer;

import android.content.Context;
import android.graphics.Bitmap;

import androidx.print.PrintHelper;

import static com.google.common.base.Preconditions.checkNotNull;

public class IntentPrintImageRequest implements PrintImageRequest {

    private final Context context;
    private final String jobName;
    private final Bitmap bitmap;
    private final int scaleMode;
    private final int colorMode;
    private final int orientation;

    private IntentPrintImageRequest(Context context,
                                    int scaleMode,
                                    String jobName,
                                    int colorMode,
                                    Bitmap bitmap,
                                    int orientation) {
        this.context = context;
        this.jobName = jobName;
        this.bitmap = bitmap;
        this.scaleMode = scaleMode;
        this.colorMode = colorMode;
        this.orientation = orientation;
    }

    public int getColorMode() {
        return colorMode;
    }

    public int getScaleMode() {
        return scaleMode;
    }

    public int getOrientation() {
        return orientation;
    }

    public Context getContext() {
        return context;
    }

    public String getJobName() {
        return jobName;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private static final String JOB_NAME_PREFIX = "Print_Image_";

        private Context context;
        private String jobName;
        private Bitmap bitmap;
        private int scaleMode = -1;
        private int colorMode = -1;
        private int orientation = -1;

        public Builder copy(IntentPrintImageRequest request) {
            setContext(request.getContext());
            setJobName(request.getJobName());
            setBitmap(request.getBitmap());
            setScaleMode(request.getScaleMode());
            setColorMode(request.getColorMode());
            setOrientation(request.getOrientation());
            return this;
        }

        public Builder setContext(Context context) {
            this.context = context;
            return this;
        }

        public Builder setJobName(String jobName) {
            this.jobName = jobName;
            return this;
        }

        public Builder setBitmap(Bitmap bitmap) {
            this.bitmap = bitmap;
            return this;
        }

        public Builder setScaleMode(int scaleMode) {
            this.scaleMode = scaleMode;
            return this;
        }

        public Builder setColorMode(int colorMode) {
            this.colorMode = colorMode;
            return this;
        }

        public Builder setOrientation(int orientation) {
            this.orientation = orientation;
            return this;
        }

        public IntentPrintImageRequest build() {
            if (isInvalidScaleMode()) {
                scaleMode = PrintHelper.SCALE_MODE_FIT;
            }
            if (isInvalidColorMode()) {
                colorMode = PrintHelper.COLOR_MODE_COLOR;
            }
            if (isInvalidOrientation()) {
                orientation = PrintHelper.ORIENTATION_PORTRAIT;
            }
            if (jobName == null) {
                jobName = JOB_NAME_PREFIX + System.nanoTime();
            }
            checkNotNull(context, "must provide context");
            checkNotNull(bitmap, "must provide bitmap");
            return new IntentPrintImageRequest(context, scaleMode, jobName, colorMode, bitmap, orientation);
        }

        private boolean isInvalidScaleMode() {
            return scaleMode != PrintHelper.SCALE_MODE_FIT &&
                    scaleMode != PrintHelper.SCALE_MODE_FILL;
        }

        private boolean isInvalidColorMode() {
            return colorMode != PrintHelper.COLOR_MODE_COLOR &&
                    colorMode != PrintHelper.COLOR_MODE_MONOCHROME;
        }

        private boolean isInvalidOrientation() {
            return orientation != PrintHelper.ORIENTATION_PORTRAIT &&
                    orientation != PrintHelper.ORIENTATION_LANDSCAPE;
        }
    }
}
