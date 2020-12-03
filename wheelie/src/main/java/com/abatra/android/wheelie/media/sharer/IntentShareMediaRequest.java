package com.abatra.android.wheelie.media.sharer;

import android.net.Uri;

import com.abatra.android.wheelie.media.MimeTypes;
import com.google.common.base.Preconditions;

public class IntentShareMediaRequest implements ShareMediaRequest {

    private final Uri mediaUri;
    private final String mimeType;
    private MediaShareCompletionListener shareCompletionListener;

    private IntentShareMediaRequest(Uri mediaUri, String mimeType) {
        this.mediaUri = mediaUri;
        this.mimeType = mimeType;
    }

    public Uri getMediaUri() {
        return mediaUri;
    }

    public String getMimeType() {
        return mimeType;
    }

    public MediaShareCompletionListener getShareCompletionListener() {
        return shareCompletionListener;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private Uri mediaUri;
        private String mimeType;
        private MediaShareCompletionListener completionListener;

        public Builder setMediaUri(Uri mediaUri) {
            this.mediaUri = mediaUri;
            return this;
        }

        public Builder setMimeType(String mimeType) {
            this.mimeType = mimeType;
            return this;
        }

        public Builder image(Uri imageUri) {
            setMediaUri(imageUri);
            return setMimeType(MimeTypes.IMAGE_ANY);
        }

        public Builder jpegImage(Uri imageUri) {
            setMediaUri(imageUri);
            return setMimeType(MimeTypes.IMAGE_JPEG);
        }

        public Builder mp4Video(Uri videoUri) {
            setMediaUri(videoUri);
            return setMimeType(MimeTypes.VIDEO_MP4);
        }

        public void setCompletionListener(MediaShareCompletionListener completionListener) {
            this.completionListener = completionListener;
        }

        public IntentShareMediaRequest build() {

            IntentShareMediaRequest intentShareMediaRequest = new IntentShareMediaRequest(
                    Preconditions.checkNotNull(mediaUri, "Must provide media uri!"),
                    Preconditions.checkNotNull(mimeType, "Must provide mime type!"));

            intentShareMediaRequest.shareCompletionListener = completionListener;

            return intentShareMediaRequest;
        }
    }
}
