package com.abatra.android.wheelie.videoEditor.cutter;

import android.os.Build;
import android.os.ParcelFileDescriptor;

import androidx.annotation.RequiresApi;

import com.abatra.android.wheelie.videoEditor.transcoder.FileDescriptorTranscodeVideoResult;
import com.abatra.android.wheelie.videoEditor.transcoder.FilePathTranscodeVideoResult;
import com.abatra.android.wheelie.videoEditor.transcoder.TranscodeVideoResult;

public interface CutVideoMiddleResult extends TranscodeVideoResult {

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    static CutVideoMiddleResult filePath(String filePath) {
        TranscodeVideoResult transcodeVideoResult = new FilePathTranscodeVideoResult(filePath);
        return transcodeVideoResult::createMediaMuxer;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    static CutVideoMiddleResult fileDescriptor(ParcelFileDescriptor parcelFileDescriptor) {
        TranscodeVideoResult transcodeVideoResult = new FileDescriptorTranscodeVideoResult(parcelFileDescriptor);
        return transcodeVideoResult::createMediaMuxer;
    }
}
