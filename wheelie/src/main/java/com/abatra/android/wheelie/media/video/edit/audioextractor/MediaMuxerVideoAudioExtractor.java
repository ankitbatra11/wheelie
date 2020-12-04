package com.abatra.android.wheelie.media.video.edit.audioextractor;

import com.abatra.android.wheelie.media.video.edit.transcoder.MediaMuxerTranscodeVideoRequest;
import com.abatra.android.wheelie.media.video.edit.transcoder.MediaMuxerVideoTranscoder;
import com.abatra.android.wheelie.media.video.edit.transcoder.VideoTranscoder;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MediaMuxerVideoAudioExtractor implements VideoAudioExtractor {

    private final VideoTranscoder videoTranscoder;

    @Inject
    MediaMuxerVideoAudioExtractor(MediaMuxerVideoTranscoder mediaMuxerVideoTranscoder) {
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
