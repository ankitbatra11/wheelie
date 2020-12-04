package com.abatra.android.wheelie.media.video.edit.transcoder;

import android.media.MediaMuxer;
import android.os.Build;
import android.os.ParcelFileDescriptor;

import androidx.annotation.RequiresApi;

import java.io.IOException;

@RequiresApi(api = Build.VERSION_CODES.O)
public class FileDescriptorTranscodeVideoResult implements TranscodeVideoResult {

    private final ParcelFileDescriptor parcelFileDescriptor;

    public FileDescriptorTranscodeVideoResult(ParcelFileDescriptor parcelFileDescriptor) {
        this.parcelFileDescriptor = parcelFileDescriptor;
    }

    @Override
    public MediaMuxer createMediaMuxer() throws IOException {
        return new MediaMuxer(parcelFileDescriptor.getFileDescriptor(), MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
    }
}
