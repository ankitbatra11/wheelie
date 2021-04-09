package com.abatra.android.wheelie.activity.result.contract;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.annotation.NonNull;

public class OpenMediaActivityResultContract extends AbstractActivityResultContract<OpenMediaActivityResultContract.OpenableMedia> {

    @NonNull
    @Override
    public Intent createIntent(@NonNull Context context, OpenableMedia openableMedia) {
        return new Intent(Intent.ACTION_VIEW)
                .setDataAndType(openableMedia.uri, openableMedia.mimeType)
                .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
    }

    public static final class OpenableMedia {

        private final Uri uri;
        private final String mimeType;

        public OpenableMedia(Uri uri, String mimeType) {
            this.uri = uri;
            this.mimeType = mimeType;
        }
    }
}
