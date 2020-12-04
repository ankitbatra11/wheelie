package com.abatra.android.wheelie.media.video.edit.transcoder;

import android.content.Context;
import android.media.MediaExtractor;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.common.base.Optional;

public interface TranscodableVideo {

    void setDataSource(Context context, MediaExtractor mediaExtractor);

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    Optional<String> getRotationDegrees(Context context);
}
