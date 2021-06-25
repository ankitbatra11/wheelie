package com.abatra.android.wheelie.videoEditor.transcoder;

import com.abatra.android.wheelie.videoEditor.AudioNotFoundException;

public interface VideoTranscoder {
    void transcodeVideo(TranscodeVideoRequest request) throws AudioNotFoundException;
}
