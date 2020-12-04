package com.abatra.android.wheelie.media.video.edit.transcoder;

import android.content.Context;
import android.media.MediaExtractor;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.google.common.base.Optional;

import java.io.IOException;

import timber.log.Timber;

public class UriTranscodableVideo implements TranscodableVideo {

    private final Uri uri;

    public UriTranscodableVideo(Uri uri) {
        this.uri = uri;
    }

    @Override
    public void setDataSource(Context context, MediaExtractor mediaExtractor) {
        try {
            mediaExtractor.setDataSource(context, uri, null);
        } catch (IOException e) {
            throw new RuntimeException(String.format("Setting uri=%s as data source failed!", uri), e);
        }
    }

    @Override
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public Optional<String> getRotationDegrees(Context context) {
        return Optional.fromNullable(getRotationDegreesSilently(context));
    }

    @Nullable
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private String getRotationDegreesSilently(Context context) {
        String rotationDegrees = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            rotationDegrees = tryGettingRotationDegrees(retriever, context);
        } catch (Throwable t) {
            Timber.e(t);
        } finally {
            retriever.release();
        }
        return rotationDegrees;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private String tryGettingRotationDegrees(MediaMetadataRetriever retriever, Context context) {
        retriever.setDataSource(context, uri);
        return retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION);
    }
}
