package com.abatra.android.wheelie.media.video.edit.cutter;

import android.content.Context;
import android.media.MediaExtractor;
import android.net.Uri;
import android.os.Build;
import android.os.ParcelFileDescriptor;

import androidx.annotation.RequiresApi;

import com.abatra.android.wheelie.media.video.edit.transcoder.FileDescriptorTranscodeVideoResult;
import com.abatra.android.wheelie.media.video.edit.transcoder.FilePathTranscodeVideoResult;
import com.abatra.android.wheelie.media.video.edit.transcoder.TranscodableVideo;
import com.abatra.android.wheelie.media.video.edit.transcoder.TranscodeVideoResult;
import com.abatra.android.wheelie.media.video.edit.transcoder.UriTranscodableVideo;
import com.google.common.base.Optional;
import com.google.common.collect.Range;

import java.util.concurrent.TimeUnit;

import static com.google.common.base.Preconditions.checkNotNull;

public class MediaMuxerCutVideoMiddleRequest implements CutVideoMiddleRequest {

    private final MiddleCutableVideo middleCutableVideo;
    private final CutVideoMiddleResult cutVideoMiddleResult;
    private final Range<Long> cutInMiddleDurationMicrosRange;

    public MediaMuxerCutVideoMiddleRequest(MiddleCutableVideo middleCutableVideo,
                                           CutVideoMiddleResult cutVideoMiddleResult,
                                           Range<Long> cutInMiddleDurationMicrosRange) {
        this.middleCutableVideo = middleCutableVideo;
        this.cutVideoMiddleResult = cutVideoMiddleResult;
        this.cutInMiddleDurationMicrosRange = cutInMiddleDurationMicrosRange;
    }

    public MiddleCutableVideo getMiddleCutableVideo() {
        return middleCutableVideo;
    }

    public CutVideoMiddleResult getCutVideoMiddleResult() {
        return cutVideoMiddleResult;
    }

    public Range<Long> getCutInMiddleDurationMicrosRange() {
        return cutInMiddleDurationMicrosRange;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private MiddleCutableVideo middleCutableVideo;
        private CutVideoMiddleResult cutVideoMiddleResult;
        private Range<Long> cutInMiddleDurationMicrosRange;

        public Builder setMiddleCutableVideo(MiddleCutableVideo middleCutableVideo) {
            this.middleCutableVideo = middleCutableVideo;
            return this;
        }

        public Builder setMiddleCutableVideo(Uri videoUri) {
            TranscodableVideo transcodableVideo = new UriTranscodableVideo(videoUri);
            this.middleCutableVideo = new MiddleCutableVideo() {
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

        public Builder setCutVideoMiddleResult(CutVideoMiddleResult cutVideoMiddleResult) {
            this.cutVideoMiddleResult = cutVideoMiddleResult;
            return this;
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
        public Builder setCutVideoMiddleResult(String filePath) {
            TranscodeVideoResult transcodeVideoResult = new FilePathTranscodeVideoResult(filePath);
            this.cutVideoMiddleResult = transcodeVideoResult::createMediaMuxer;
            return this;
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        public Builder setCutVideoMiddleResult(ParcelFileDescriptor parcelFileDescriptor) {
            TranscodeVideoResult transcodeVideoResult = new FileDescriptorTranscodeVideoResult(parcelFileDescriptor);
            this.cutVideoMiddleResult = transcodeVideoResult::createMediaMuxer;
            return this;
        }

        public Builder setCutInMiddleDurationMicrosRange(Range<Long> cutInMiddleDurationMicrosRange) {
            this.cutInMiddleDurationMicrosRange = cutInMiddleDurationMicrosRange;
            return this;
        }

        public Builder setCutInMiddleDurationClosedRange(long start, long end, TimeUnit timeUnit) {
            return setCutInMiddleDurationMicrosRange(Range.closed(timeUnit.toMicros(start), timeUnit.toMicros(end)));
        }

        public MediaMuxerCutVideoMiddleRequest build() {
            return new MediaMuxerCutVideoMiddleRequest(
                    checkNotNull(middleCutableVideo, "Must provide cut able video"),
                    checkNotNull(cutVideoMiddleResult, "Must provide cut video result"),
                    checkCutInMiddleDurationRange());
        }

        private Range<Long> checkCutInMiddleDurationRange() {
            if (cutInMiddleDurationMicrosRange == null ||
                    cutInMiddleDurationMicrosRange.lowerEndpoint() <= 0 ||
                    cutInMiddleDurationMicrosRange.upperEndpoint() <= 0 ||
                    cutInMiddleDurationMicrosRange.lowerEndpoint() >= cutInMiddleDurationMicrosRange.upperEndpoint()) {
                throw new IllegalArgumentException("Invalid cut in middle duration millis range=" + cutInMiddleDurationMicrosRange);
            }
            return cutInMiddleDurationMicrosRange;
        }
    }
}
