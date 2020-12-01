package com.abatra.android.wheelie.media.printer;

import androidx.print.PrintHelper;

public class IntentImagePrinter implements ImagePrinter {

    private static final IntentImagePrinter INSTANCE = new IntentImagePrinter();

    private IntentImagePrinter() {
    }

    public static IntentImagePrinter getInstance() {
        return INSTANCE;
    }

    @Override
    public void print(PrintImageRequest printImageRequest) {
        IntentPrintImageRequest request = (IntentPrintImageRequest) printImageRequest;
        PrintHelper printHelper = new PrintHelper(request.getContext());
        printHelper.printBitmap(request.getJobName(), request.getBitmap());
    }
}
