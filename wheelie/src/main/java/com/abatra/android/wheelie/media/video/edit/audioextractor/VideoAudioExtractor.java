package com.abatra.android.wheelie.media.video.edit.audioextractor;

import com.abatra.android.wheelie.media.video.edit.AudioNotFoundException;

public interface VideoAudioExtractor {
    void extractVideoAudio(ExtractVideoAudioRequest request) throws AudioNotFoundException;
}
