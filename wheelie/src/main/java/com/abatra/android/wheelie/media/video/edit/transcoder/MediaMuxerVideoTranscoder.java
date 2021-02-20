package com.abatra.android.wheelie.media.video.edit.transcoder;

import android.content.Context;
import android.media.MediaCodec;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.os.Build;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.abatra.android.wheelie.media.video.edit.AudioNotFoundException;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;

import timber.log.Timber;

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
public class MediaMuxerVideoTranscoder implements VideoTranscoder {

    @SuppressWarnings("PointlessArithmeticExpression")
    private static final int DEFAULT_BUFFER_SIZE = 1 * 1024 * 1024;

    private final Context context;

    public MediaMuxerVideoTranscoder(Context context) {
        this.context = context;
    }

    @Override
    public void transcodeVideo(TranscodeVideoRequest request) {

        MediaMuxerTranscodeVideoRequest videoRequest = (MediaMuxerTranscodeVideoRequest) request;

        // Set up MediaExtractor to read from the source.
        MediaExtractor extractor = new MediaExtractor();
        videoRequest.getTranscodableVideo().setDataSource(context, extractor);
        try {
            tryTranscodingVideo(extractor, videoRequest);
        } catch (IOException e) {
            throw new RuntimeException("Transcoding video failed for request=" + videoRequest, e);
        } finally {
            extractor.release();
        }
    }

    private void tryTranscodingVideo(MediaExtractor extractor, MediaMuxerTranscodeVideoRequest request) throws IOException {

        int trackCount = extractor.getTrackCount();

        // Set up MediaMuxer for the destination.
        MediaMuxer muxer = request.getTranscodeVideoResult().createMediaMuxer();

        // Set up the tracks and retrieve the max buffer size for selected
        // tracks.
        HashMap<Integer, Integer> indexMap = new HashMap<>(trackCount);
        boolean audioTrackSelected = false;
        int bufferSize = -1;
        for (int i = 0; i < trackCount; i++) {
            MediaFormat format = extractor.getTrackFormat(i);
            boolean selectCurrentTrack = false;
            String mime = format.getString(MediaFormat.KEY_MIME);
            if (mime != null) {
                if (mime.startsWith("audio/") && request.isTranscodeAudio()) {
                    selectCurrentTrack = true;
                    audioTrackSelected = true;
                } else if (mime.startsWith("video/") && request.isTranscodeVideo()) {
                    selectCurrentTrack = true;
                }
            }
            if (selectCurrentTrack) {
                extractor.selectTrack(i);
                int dstIndex = muxer.addTrack(format);
                indexMap.put(i, dstIndex);
                if (format.containsKey(MediaFormat.KEY_MAX_INPUT_SIZE)) {
                    int newSize = format.getInteger(MediaFormat.KEY_MAX_INPUT_SIZE);
                    bufferSize = Math.max(newSize, bufferSize);
                }
            }
        }
        if (transcodeOnlyAudio(request) && !audioTrackSelected) {
            throw new AudioNotFoundException();
        }
        if (bufferSize < 0) {
            bufferSize = DEFAULT_BUFFER_SIZE;
        }
        String degreesString = request.getTranscodableVideo().getRotationDegrees(context).orNull();
        if (degreesString != null) {
            int degrees = Integer.parseInt(degreesString);
            if (degrees >= 0) {
                muxer.setOrientationHint(degrees);
            }
        }

        if (request.getTrimStartDurationMillis() > 0) {
            extractor.seekTo(request.getTrimStartDurationMillis() * 1000, MediaExtractor.SEEK_TO_CLOSEST_SYNC);
        }

        // Copy the samples from MediaExtractor to MediaMuxer. We will loop
        // for copying each sample and stop when we get to the end of the source
        // file or exceed the end time of the trimming.
        int offset = 0;
        int trackIndex;
        ByteBuffer dstBuf = ByteBuffer.allocate(bufferSize);
        MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
        try {
            muxer.start();
            while (true) {
                bufferInfo.offset = offset;
                bufferInfo.size = extractor.readSampleData(dstBuf, offset);
                if (bufferInfo.size < 0) {
                    Timber.d("Saw input EOS.");
                    bufferInfo.size = 0;
                    break;
                } else {
                    bufferInfo.presentationTimeUs = extractor.getSampleTime();
                    int trimEndDurationMillis = request.getTrimEndDurationMillis();
                    if (trimEndDurationMillis > 0 && bufferInfo.presentationTimeUs > (trimEndDurationMillis * 1000)) {
                        Timber.d("The current sample is over the trim end time.");
                        break;
                    } else {
                        bufferInfo.flags = extractor.getSampleFlags();
                        trackIndex = extractor.getSampleTrackIndex();
                        Integer nullableTrackIndex = indexMap.get(trackIndex);
                        if (includeBufferInResult(nullableTrackIndex, bufferInfo.presentationTimeUs, request)) {
                            muxer.writeSampleData(nullableTrackIndex, dstBuf, bufferInfo);
                        }
                        extractor.advance();
                    }
                }
            }

            muxer.stop();
        } catch (IllegalStateException e) {
            // Swallow the exception due to malformed source.
            Timber.w(e, "The source video file is malformed");
        } finally {
            muxer.release();
        }
    }

    private boolean transcodeOnlyAudio(MediaMuxerTranscodeVideoRequest request) {
        return request.isTranscodeAudio() && !request.isTranscodeVideo();
    }

    private boolean includeBufferInResult(@Nullable Integer trackIndex,
                                          long presentationTimeUs,
                                          MediaMuxerTranscodeVideoRequest request) {
        return trackIndex != null && !inCutMiddleRange(presentationTimeUs, request);
    }

    private boolean inCutMiddleRange(long presentationTimeUs, MediaMuxerTranscodeVideoRequest request) {
        return request.getCutInMiddleDurationMicrosRange().isPresent() &&
                request.getCutInMiddleDurationMicrosRange().get().contains(presentationTimeUs);
    }
}
