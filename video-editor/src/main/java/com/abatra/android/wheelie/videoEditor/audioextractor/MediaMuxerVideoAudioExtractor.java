package com.abatra.android.wheelie.videoEditor.audioextractor;

import com.abatra.android.wheelie.videoEditor.transcoder.MediaMuxerTranscodeVideoRequest;
import com.abatra.android.wheelie.videoEditor.transcoder.MediaMuxerVideoTranscoder;
import com.abatra.android.wheelie.videoEditor.transcoder.VideoTranscoder;

public class MediaMuxerVideoAudioExtractor implements VideoAudioExtractor {

    private final VideoTranscoder videoTranscoder;

    public MediaMuxerVideoAudioExtractor(MediaMuxerVideoTranscoder mediaMuxerVideoTranscoder) {
        this.videoTranscoder = mediaMuxerVideoTranscoder;
    }

    @Override
    public void extractVideoAudio(ExtractVideoAudioRequest request) {
        MediaMuxerExtractVideoAudioRequest audioRequest = (MediaMuxerExtractVideoAudioRequest) request;
        videoTranscoder.transcodeVideo(MediaMuxerTranscodeVideoRequest.builder()
                .setTranscodableVideo(audioRequest.getAudioExtractableVideo())
                .setTranscodeVideo(false)
                .setTranscodeVideoResult(audioRequest.getExtractVideoAudioResult())
                .build());
    }
}
