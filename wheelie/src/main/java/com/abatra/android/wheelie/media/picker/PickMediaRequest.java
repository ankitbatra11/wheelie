package com.abatra.android.wheelie.media.picker;

import android.net.Uri;

import androidx.activity.result.ActivityResultCallback;
import androidx.annotation.Nullable;

import java.util.List;

public class PickMediaRequest {

    private final PickableMediaType pickableMediaType;
    private final PickMediaCount pickMediaCount;

    @Nullable
    private ActivityResultCallback<List<Uri>> multipleMediaResultCallback;

    @Nullable
    private ActivityResultCallback<Uri> mediaResultCallback;

    private PickMediaRequest(PickableMediaType pickableMediaType, PickMediaCount pickMediaCount) {
        this.pickableMediaType = pickableMediaType;
        this.pickMediaCount = pickMediaCount;
    }

    @Nullable
    public ActivityResultCallback<List<Uri>> getMultipleMediaResultCallback() {
        return multipleMediaResultCallback;
    }

    @Nullable
    public ActivityResultCallback<Uri> getMediaResultCallback() {
        return mediaResultCallback;
    }

    public PickableMediaType getPickableMediaType() {
        return pickableMediaType;
    }

    public PickMediaCount getPickMediaCount() {
        return pickMediaCount;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private PickableMediaType pickableMediaType;
        private PickMediaCount pickMediaCount;
        private ActivityResultCallback<List<Uri>> multipleMediaResultCallback;
        private ActivityResultCallback<Uri> mediaResultCallback;

        public Builder pick(PickMediaCount pickMediaCount) {
            this.pickMediaCount = pickMediaCount;
            return this;
        }

        public Builder ofType(PickableMediaType pickableMediaType) {
            this.pickableMediaType = pickableMediaType;
            return this;
        }

        public Builder withMultipleMediaResultCallback(ActivityResultCallback<List<Uri>> multipleMediaResultCallback) {
            this.multipleMediaResultCallback = multipleMediaResultCallback;
            return this;
        }

        public Builder withMediaResultCallback(ActivityResultCallback<Uri> mediaResultCallback) {
            this.mediaResultCallback = mediaResultCallback;
            return this;
        }

        public PickMediaRequest build() {
            PickMediaRequest pickMediaRequest = new PickMediaRequest(pickableMediaType, pickMediaCount);
            pickMediaRequest.multipleMediaResultCallback = multipleMediaResultCallback;
            pickMediaRequest.mediaResultCallback = mediaResultCallback;
            return pickMediaRequest;
        }
    }
}
