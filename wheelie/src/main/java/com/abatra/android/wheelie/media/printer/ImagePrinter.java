package com.abatra.android.wheelie.media.printer;

public interface ImagePrinter {

    void print(PrintImageRequest printImageRequest);

    void print(PrintImageRequest printImageRequest, Listener listener);

    interface Listener {
        void onPrintingImageFinished();
    }
}