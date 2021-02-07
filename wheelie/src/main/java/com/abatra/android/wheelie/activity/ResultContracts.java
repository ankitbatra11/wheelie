package com.abatra.android.wheelie.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.abatra.android.wheelie.media.MimeTypes;
import com.abatra.android.wheelie.util.IntentUtils;

public final class ResultContracts {

    private ResultContracts() {
    }

    private static abstract class ActivityResultActivityResultContract<I> extends ActivityResultContract<I, ActivityResult> {
        @Override
        public ActivityResult parseResult(int resultCode, @Nullable Intent intent) {
            return new ActivityResult(resultCode, intent);
        }
    }

    public static class AttachData extends ActivityResultActivityResultContract<AttachData.Data> {

        @NonNull
        @Override
        public Intent createIntent(@NonNull Context context, Data input) {
            return new Intent(Intent.ACTION_ATTACH_DATA)
                    .setDataAndType(input.getUri(), input.getMimeType())
                    .setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }

        @Override
        public ActivityResult parseResult(int resultCode, @Nullable Intent intent) {
            return new ActivityResult(resultCode, intent);
        }

        public static class Data {

            private final String mimeType;
            private final Uri uri;

            public Data(String mimeType, Uri uri) {
                this.mimeType = mimeType;
                this.uri = uri;
            }

            public static Data mp4Video(Uri uri) {
                return new Data(MimeTypes.VIDEO_MP4, uri);
            }

            public static Data image(Uri uri) {
                return new Data(MimeTypes.IMAGE_ANY, uri);
            }

            public String getMimeType() {
                return mimeType;
            }

            public Uri getUri() {
                return uri;
            }
        }
    }

    public static class OpenSettingsScreen extends ActivityResultActivityResultContract<Void> {

        private final String action;

        private OpenSettingsScreen(String action) {
            this.action = action;
        }

        public static OpenSettingsScreen wirelessSettings() {
            return new OpenSettingsScreen(Settings.ACTION_WIRELESS_SETTINGS);
        }

        @NonNull
        @Override
        public Intent createIntent(@NonNull Context context, Void input) {
            Intent intent = new Intent(action);
            if (!IntentUtils.isLaunchable(intent, context)) {
                intent = new Intent(Settings.ACTION_SETTINGS);
            }
            return intent;
        }
    }
}
