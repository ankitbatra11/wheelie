package com.abatra.android.wheelie.activity.result.contract;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.activity.result.ActivityResult;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import static com.abatra.android.wheelie.activity.result.contract.AttachDataActivityResultContract.*;

public class AttachDataActivityResultContract extends AbstractActivityResultContract<AttachableData> {

    @NonNull
    @Override
    public Intent createIntent(@NonNull Context context, AttachableData attachableData) {
        return new Intent(Intent.ACTION_ATTACH_DATA)
                .setDataAndType(attachableData.uri, attachableData.mimeType)
                .setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
    }

    @Override
    public ActivityResult parseResult(int resultCode, @Nullable Intent intent) {
        return new ActivityResult(resultCode, intent);
    }

    public static final class AttachableData {

        private final Uri uri;
        private final String mimeType;

        public AttachableData(Uri uri, String mimeType) {
            this.uri = uri;
            this.mimeType = mimeType;
        }
    }
}
