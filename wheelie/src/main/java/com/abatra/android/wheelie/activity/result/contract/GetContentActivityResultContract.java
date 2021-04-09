package com.abatra.android.wheelie.activity.result.contract;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.abatra.android.wheelie.media.MimeTypes;

import java.util.Optional;

public class GetContentActivityResultContract extends ActivityResultContract<Void, Optional<Uri>> {

    private final String mimeType;
    private final ActivityResultContracts.GetContent getContent = new ActivityResultContracts.GetContent();

    public GetContentActivityResultContract(String mimeType) {
        this.mimeType = mimeType;
    }

    public static GetContentActivityResultContract anyImage() {
        return new GetContentActivityResultContract(MimeTypes.IMAGE_ANY);
    }

    public static GetContentActivityResultContract mp4Video() {
        return new GetContentActivityResultContract(MimeTypes.VIDEO_MP4);
    }

    @NonNull
    @Override
    public Intent createIntent(@NonNull Context context, Void input) {
        return getContent.createIntent(context, mimeType);
    }

    @Override
    public Optional<Uri> parseResult(int resultCode, @Nullable Intent intent) {
        return Optional.ofNullable(getContent.parseResult(resultCode, intent));
    }
}
