package com.abatra.android.wheelie.media.picker;

import android.net.Uri;

import androidx.activity.result.ActivityResultCallback;

import com.abatra.android.wheelie.activity.ActivityResultRegistrar;

import java.util.List;

public class PickMediaRequest {

    private final PickableMediaType pickableMediaType;
    private final PickMediaCount pickMediaCount;
    private ActivityResultRegistrar activityResultRegistrar;
    private ActivityResultCallback<List<Uri>> multipleMediaResultCallback;

    private PickMediaRequest(PickableMediaType pickableMediaType, PickMediaCount pickMediaCount) {
        this.pickableMediaType = pickableMediaType;
        this.pickMediaCount = pickMediaCount;
    }

    public ActivityResultCallback<List<Uri>> getMultipleMediaResultCallback() {
        return multipleMediaResultCallback;
    }

    public ActivityResultRegistrar getActivityResultRegistrar() {
        return activityResultRegistrar;
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
        private ActivityResultRegistrar activityResultRegistrar;
        private ActivityResultCallback<List<Uri>> multipleMediaResultCallback;

        public Builder pick(PickMediaCount pickMediaCount) {
            this.pickMediaCount = pickMediaCount;
            return this;
        }

        public Builder ofType(PickableMediaType pickableMediaType) {
            this.pickableMediaType = pickableMediaType;
            return this;
        }

        public Builder withActivityResultRegistrar(ActivityResultRegistrar activityResultRegistrar) {
            this.activityResultRegistrar = activityResultRegistrar;
            return this;
        }

        public Builder withMultipleMediaResultCallback(ActivityResultCallback<List<Uri>> multipleMediaResultCallback) {
            this.multipleMediaResultCallback = multipleMediaResultCallback;
            return this;
        }

        public PickMediaRequest build() {
            PickMediaRequest pickMediaRequest = new PickMediaRequest(pickableMediaType, pickMediaCount);
            pickMediaRequest.activityResultRegistrar = activityResultRegistrar;
            pickMediaRequest.multipleMediaResultCallback = multipleMediaResultCallback;
            return pickMediaRequest;
        }
    }
}
