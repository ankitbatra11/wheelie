package com.abatra.android.wheelie.videoEditor;

public class AudioNotFoundException extends RuntimeException {

    public AudioNotFoundException() {
        super("Audio not found in video");
    }
}
