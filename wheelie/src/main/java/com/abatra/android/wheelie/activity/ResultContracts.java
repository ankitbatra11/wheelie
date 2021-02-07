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

    public static class MediaInfo {

        private final String mimeType;
        private final Uri uri;

        public MediaInfo(String mimeType, Uri uri) {
            this.mimeType = mimeType;
            this.uri = uri;
        }

        public static MediaInfo mp4Video(Uri uri) {
            return new MediaInfo(MimeTypes.VIDEO_MP4, uri);
        }

        public static MediaInfo image(Uri uri) {
            return new MediaInfo(MimeTypes.IMAGE_ANY, uri);
        }

        public String getMimeType() {
            return mimeType;
        }

        public Uri getUri() {
            return uri;
        }
    }

    public static class OpenMedia extends ActivityResultActivityResultContract<MediaInfo> {

        @NonNull
        @Override
        public Intent createIntent(@NonNull Context context, MediaInfo input) {
            return new Intent(Intent.ACTION_VIEW)
                    .setDataAndType(input.getUri(), input.getMimeType())
                    .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
    }

    public static class ShareMedia extends ActivityResultActivityResultContract<MediaInfo> {

        @NonNull
        @Override
        public Intent createIntent(@NonNull Context context, MediaInfo input) {
            return new Intent(Intent.ACTION_SEND)
                    .setType(input.getMimeType())
                    .putExtra(Intent.EXTRA_STREAM, input.getUri())
                    .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
    }

    public static class AttachData extends ActivityResultActivityResultContract<MediaInfo> {

        @NonNull
        @Override
        public Intent createIntent(@NonNull Context context, MediaInfo input) {
            return new Intent(Intent.ACTION_ATTACH_DATA)
                    .setDataAndType(input.getUri(), input.getMimeType())
                    .setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }

        @Override
        public ActivityResult parseResult(int resultCode, @Nullable Intent intent) {
            return new ActivityResult(resultCode, intent);
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
