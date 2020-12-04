package com.abatra.android.wheelie.media.video.edit.audioextractor;

import android.os.Build;
import android.os.ParcelFileDescriptor;

import androidx.annotation.RequiresApi;

import com.abatra.android.wheelie.media.video.edit.transcoder.FileDescriptorTranscodeVideoResult;
import com.abatra.android.wheelie.media.video.edit.transcoder.FilePathTranscodeVideoResult;
import com.abatra.android.wheelie.media.video.edit.transcoder.TranscodeVideoResult;

public interface ExtractVideoAudioResult extends TranscodeVideoResult {

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    static ExtractVideoAudioResult filePath(String filePath) {
        TranscodeVideoResult transcodeVideoResult = new FilePathTranscodeVideoResult(filePath);
        return transcodeVideoResult::createMediaMuxer;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    static ExtractVideoAudioResult fileDescriptor(ParcelFileDescriptor parcelFileDescriptor) {
        TranscodeVideoResult transcodeVideoResult = new FileDescriptorTranscodeVideoResult(parcelFileDescriptor);
        return transcodeVideoResult::createMediaMuxer;
    }
}
