package com.abatra.android.wheelie.media.video.edit.transcoder;

import com.abatra.android.wheelie.media.video.edit.AudioNotFoundException;

public interface VideoTranscoder {
    void transcodeVideo(TranscodeVideoRequest request) throws AudioNotFoundException;
}
