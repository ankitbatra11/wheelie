package com.abatra.android.wheelie.core.activity.resultContracts;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.annotation.NonNull;

public class ShareMediaActivityResultContract extends AbstractActivityResultContract<ShareMediaActivityResultContract.ShareableMedia> {

    @NonNull
    @Override
    public Intent createIntent(@NonNull Context context, ShareableMedia input) {
        return new Intent(Intent.ACTION_SEND)
                .setType(input.mimeType)
                .putExtra(Intent.EXTRA_STREAM, input.uri)
                .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
    }

    public static final class ShareableMedia {

        private final String mimeType;
        private final Uri uri;

        public ShareableMedia(String mimeType, Uri uri) {
            this.mimeType = mimeType;
            this.uri = uri;
        }
    }
}
