package com.abatra.android.wheelie.media.video.edit.transcoder;

import android.media.MediaMuxer;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.IOException;

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
public class FilePathTranscodeVideoResult implements TranscodeVideoResult {

    private final String filePath;

    public FilePathTranscodeVideoResult(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public MediaMuxer createMediaMuxer() throws IOException {
        return new MediaMuxer(filePath, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
    }
}
