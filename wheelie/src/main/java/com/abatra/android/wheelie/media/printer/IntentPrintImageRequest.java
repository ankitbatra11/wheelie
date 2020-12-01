package com.abatra.android.wheelie.media.printer;

import android.content.Context;
import android.graphics.Bitmap;

public class IntentPrintImageRequest implements PrintImageRequest {

    private final Context context;
    private final String jobName;
    private final Bitmap bitmap;

    public IntentPrintImageRequest(Context context, String jobName, Bitmap bitmap) {
        this.context = context;
        this.jobName = jobName;
        this.bitmap = bitmap;
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
}
