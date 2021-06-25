package com.abatra.android.wheelie.picker;

public enum PickableMediaType {
    IMAGE {
        @Override
        public String getMimeType() {
            return MIME_TYPE_ANY_IMAGE;
        }
    };

    private static final String MIME_TYPE_ANY_IMAGE = "image/*";

    public abstract String getMimeType();
}
