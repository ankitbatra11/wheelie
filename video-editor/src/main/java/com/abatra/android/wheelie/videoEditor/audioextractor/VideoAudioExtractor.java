package com.abatra.android.wheelie.videoEditor.audioextractor;

import com.abatra.android.wheelie.videoEditor.AudioNotFoundException;

public interface VideoAudioExtractor {
    void extractVideoAudio(ExtractVideoAudioRequest request) throws AudioNotFoundException;
}
