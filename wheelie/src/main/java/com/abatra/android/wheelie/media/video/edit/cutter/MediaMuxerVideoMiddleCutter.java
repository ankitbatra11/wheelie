package com.abatra.android.wheelie.media.video.edit.cutter;

import com.abatra.android.wheelie.media.video.edit.transcoder.MediaMuxerTranscodeVideoRequest;
import com.abatra.android.wheelie.media.video.edit.transcoder.MediaMuxerVideoTranscoder;
import com.abatra.android.wheelie.media.video.edit.transcoder.VideoTranscoder;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MediaMuxerVideoMiddleCutter implements VideoMiddleCutter {

    private final VideoTranscoder videoTranscoder;

    @Inject
    MediaMuxerVideoMiddleCutter(MediaMuxerVideoTranscoder mediaMuxerVideoTranscoder) {
        videoTranscoder = mediaMuxerVideoTranscoder;
    }

    @Override
    public void cutVideoMiddle(CutVideoMiddleRequest request) {
        MediaMuxerCutVideoMiddleRequest middleRequest = (MediaMuxerCutVideoMiddleRequest) request;
        videoTranscoder.transcodeVideo(MediaMuxerTranscodeVideoRequest.builder()
                .setTranscodableVideo(middleRequest.getMiddleCutableVideo())
                .setCutInMiddleDurationMicrosRange(middleRequest.getCutInMiddleDurationMicrosRange())
                .setTranscodeVideoResult(middleRequest.getCutVideoMiddleResult())
                .build());
    }
}
