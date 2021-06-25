package com.abatra.android.wheelie.videoEditor.audioextractor;

import android.content.Context;
import android.media.MediaExtractor;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.abatra.android.wheelie.videoEditor.transcoder.TranscodableVideo;
import com.abatra.android.wheelie.videoEditor.transcoder.UriTranscodableVideo;
import com.google.common.base.Optional;

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

        public Builder setAudioExtractableVideo(Uri videoUri) {
            TranscodableVideo transcodableVideo = new UriTranscodableVideo(videoUri);
            this.audioExtractableVideo = new AudioExtractableVideo() {
                @Override
                public void setDataSource(Context context, MediaExtractor mediaExtractor) {
                    transcodableVideo.setDataSource(context, mediaExtractor);
                }

                @Override
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
                public Optional<String> getRotationDegrees(Context context) {
                    return transcodableVideo.getRotationDegrees(context);
                }
            };
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
