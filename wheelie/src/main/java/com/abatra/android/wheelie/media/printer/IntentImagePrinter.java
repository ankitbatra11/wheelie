package com.abatra.android.wheelie.media.printer;

import androidx.annotation.Nullable;
import androidx.print.PrintHelper;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class IntentImagePrinter implements ImagePrinter {

    @Inject
    public IntentImagePrinter() {
    }

    @Override
    public void print(PrintImageRequest printImageRequest) {
        print(printImageRequest, null);
    }

    @Override
    public void print(PrintImageRequest printImageRequest, @Nullable Listener listener) {
        IntentPrintImageRequest request = (IntentPrintImageRequest) printImageRequest;
        PrintHelper printHelper = new PrintHelper(request.getContext());
        printHelper.setScaleMode(request.getScaleMode());
        printHelper.setOrientation(request.getOrientation());
        printHelper.setColorMode(request.getColorMode());
        if (listener != null) {
            printHelper.printBitmap(request.getJobName(), request.getBitmap(), listener::onPrintingImageFinished);
        } else {
            printHelper.printBitmap(request.getJobName(), request.getBitmap());
        }

    }
}
