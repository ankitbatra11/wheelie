package com.abatra.android.wheelie.media.video.edit.transcoder;

import androidx.annotation.Nullable;

import com.google.common.base.Optional;
import com.google.common.collect.Range;

import static com.google.common.base.Preconditions.checkNotNull;

public class MediaMuxerTranscodeVideoRequest implements TranscodeVideoRequest {

    private final TranscodableVideo transcodableVideo;
    private final TranscodeVideoResult transcodeVideoResult;
    private final boolean transcodeVideo;
    private final boolean transcodeAudio;
    private final int trimStartDurationMillis;
    private final int trimEndDurationMillis;
    @Nullable
    private Range<Long> cutInMiddleDurationMicrosRange;

    private MediaMuxerTranscodeVideoRequest(TranscodableVideo transcodableVideo, TranscodeVideoResult transcodeVideoResult,
                                            boolean transcodeVideo, boolean transcodeAudio,
                                            int trimStartDurationMillis, int trimEndDurationMillis) {
        this.transcodableVideo = transcodableVideo;
        this.transcodeVideoResult = transcodeVideoResult;
        this.transcodeVideo = transcodeVideo;
        this.transcodeAudio = transcodeAudio;
        this.trimStartDurationMillis = trimStartDurationMillis;
        this.trimEndDurationMillis = trimEndDurationMillis;
    }

    public TranscodableVideo getTranscodableVideo() {
        return transcodableVideo;
    }

    public TranscodeVideoResult getTranscodeVideoResult() {
        return transcodeVideoResult;
    }

    public boolean isTranscodeVideo() {
        return transcodeVideo;
    }

    public boolean isTranscodeAudio() {
        return transcodeAudio;
    }

    public int getTrimStartDurationMillis() {
        return trimStartDurationMillis;
    }

    public int getTrimEndDurationMillis() {
        return trimEndDurationMillis;
    }

    public Optional<Range<Long>> getCutInMiddleDurationMicrosRange() {
        return Optional.fromNullable(cutInMiddleDurationMicrosRange);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private TranscodableVideo transcodableVideo;
        private TranscodeVideoResult transcodeVideoResult;
        private boolean transcodeVideo = true;
        private boolean transcodeAudio = true;
        private int trimStartDurationMillis = 0;
        private int trimEndDurationMillis = 0;
        private Range<Long> cutInMiddleDurationMicrosRange;

        public Builder setTranscodableVideo(TranscodableVideo transcodableVideo) {
            this.transcodableVideo = transcodableVideo;
            return this;
        }

        public Builder setTranscodeVideoResult(TranscodeVideoResult transcodeVideoResult) {
            this.transcodeVideoResult = transcodeVideoResult;
            return this;
        }

        public Builder setTranscodeVideo(boolean transcodeVideo) {
            this.transcodeVideo = transcodeVideo;
            return this;
        }

        public Builder setTranscodeAudio(boolean transcodeAudio) {
            this.transcodeAudio = transcodeAudio;
            return this;
        }

        public Builder setTrimStartDurationMillis(int trimStartDurationMillis) {
            this.trimStartDurationMillis = trimStartDurationMillis;
            return this;
        }

        public Builder setTrimEndDurationMillis(int trimEndDurationMillis) {
            this.trimEndDurationMillis = trimEndDurationMillis;
            return this;
        }

        public Builder setCutInMiddleDurationMicrosRange(Range<Long> cutInMiddleDurationMicrosRange) {
            this.cutInMiddleDurationMicrosRange = cutInMiddleDurationMicrosRange;
            return this;
        }

        public MediaMuxerTranscodeVideoRequest build() {
            MediaMuxerTranscodeVideoRequest mediaMuxerTranscodeVideoRequest = new MediaMuxerTranscodeVideoRequest(
                    checkNotNull(transcodableVideo, "Must provide source video"),
                    checkNotNull(transcodeVideoResult, "Must provide transcode video result"),
                    transcodeVideo,
                    transcodeAudio,
                    trimStartDurationMillis,
                    trimEndDurationMillis);

            mediaMuxerTranscodeVideoRequest.cutInMiddleDurationMicrosRange = cutInMiddleDurationMicrosRange;

            return mediaMuxerTranscodeVideoRequest;
        }
    }
}
