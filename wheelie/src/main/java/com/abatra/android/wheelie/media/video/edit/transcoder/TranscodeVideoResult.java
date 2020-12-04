package com.abatra.android.wheelie.media.video.edit.transcoder;

import android.media.MediaMuxer;

import java.io.IOException;

public interface TranscodeVideoResult {
    MediaMuxer createMediaMuxer() throws IOException;
}
