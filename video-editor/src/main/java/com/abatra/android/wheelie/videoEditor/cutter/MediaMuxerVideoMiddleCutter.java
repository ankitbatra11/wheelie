package com.abatra.android.wheelie.videoEditor.cutter;

import com.abatra.android.wheelie.videoEditor.transcoder.MediaMuxerTranscodeVideoRequest;
import com.abatra.android.wheelie.videoEditor.transcoder.MediaMuxerVideoTranscoder;
import com.abatra.android.wheelie.videoEditor.transcoder.VideoTranscoder;

public class MediaMuxerVideoMiddleCutter implements VideoMiddleCutter {

    private final VideoTranscoder videoTranscoder;

    public MediaMuxerVideoMiddleCutter(MediaMuxerVideoTranscoder mediaMuxerVideoTranscoder) {
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
