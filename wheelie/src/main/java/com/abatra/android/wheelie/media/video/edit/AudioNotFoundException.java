package com.abatra.android.wheelie.media.video.edit;

public class AudioNotFoundException extends RuntimeException {

    public AudioNotFoundException() {
        super("Audio not found in video");
    }
}
