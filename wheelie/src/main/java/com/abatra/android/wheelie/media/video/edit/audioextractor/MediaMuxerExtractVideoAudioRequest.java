package com.abatra.android.wheelie.media.video.edit.audioextractor;

import com.abatra.android.wheelie.media.video.edit.transcoder.FilePathTranscodeVideoResult;
import com.abatra.android.wheelie.media.video.edit.transcoder.TranscodableVideo;

import static com.google.common.base.Preconditions.checkNotNull;

public class MediaMuxerExtractVideoAudioRequest implements ExtractVideoAudioRequest {

    private final AudioExtractableVideo audioExtractableVideo;
    private final ExtractVideoAudioResult extractVideoAudioResult;

    public MediaMuxerExtractVideoAudioRequest(AudioExtractableVideo audioExtractableVideo,
                                              ExtractVideoAudioResult extractVideoAudioResult) {
        this.audioExtractableVideo = audioExtractableVideo;
        this.extractVideoAudioResult = extractVideoAudioResult;
    }

    public AudioExtractableVideo getAudioExtractableVideo() {
        return audioExtractableVideo;
    }

    public ExtractVideoAudioResult getExtractVideoAudioResult() {
        return extractVideoAudioResult;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private AudioExtractableVideo audioExtractableVideo;
        private ExtractVideoAudioResult extractVideoAudioResult;

        public Builder setAudioExtractableVideo(AudioExtractableVideo audioExtractableVideo) {
            this.audioExtractableVideo = audioExtractableVideo;
            return this;
        }

        public Builder setExtractVideoAudioResult(ExtractVideoAudioResult extractVideoAudioResult) {
            this.extractVideoAudioResult = extractVideoAudioResult;
            return this;
        }

        public MediaMuxerExtractVideoAudioRequest build() {
            return new MediaMuxerExtractVideoAudioRequest(
                    checkNotNull(audioExtractableVideo, "Must provide audio extract able video"),
                    checkNotNull(extractVideoAudioResult, "Must provide extract audio result"));
        }
    }
}
