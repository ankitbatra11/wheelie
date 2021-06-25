package com.abatra.android.wheelie.videoEditor.transcoder;

import android.media.MediaMuxer;

import java.io.IOException;

public interface TranscodeVideoResult {
    MediaMuxer createMediaMuxer() throws IOException;
}
