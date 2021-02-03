package com.abatra.android.wheelie.media.sharer;

import android.content.Context;
import android.content.Intent;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.abatra.android.wheelie.lifecycle.ILifecycleObserver;
import com.abatra.android.wheelie.lifecycle.ILifecycleOwner;

import javax.inject.Inject;

public class IntentMediaSharer implements MediaSharer, ILifecycleObserver {

    private ActivityResultLauncher<IntentShareMediaRequest> activityResultLauncher;
    private MediaShareCompletionListener shareCompletionListener;

    @Inject
    IntentMediaSharer() {
    }

    @Override
    public void observeLifecycle(ILifecycleOwner lifecycleOwner) {
        activityResultLauncher = lifecycleOwner.registerForActivityResult(ShareMediaContract.INSTANCE, result -> {
            if (shareCompletionListener != null) {
                shareCompletionListener.mediaShareCompleted();
            }
        });
    }

    @Override
    public void shareMedia(ShareMediaRequest request) {
        IntentShareMediaRequest mediaRequest = (IntentShareMediaRequest) request;
        shareCompletionListener = mediaRequest.getShareCompletionListener();
        activityResultLauncher.launch(mediaRequest);
    }

    @Override
    public void onDestroy() {
        shareCompletionListener = null;
        activityResultLauncher = null;
    }

    private static class ShareMediaContract extends ActivityResultContract<IntentShareMediaRequest, Void> {

        private static final ShareMediaContract INSTANCE = new ShareMediaContract();

        @NonNull
        @Override
        public Intent createIntent(@NonNull Context context, IntentShareMediaRequest input) {
            return new Intent(Intent.ACTION_SEND)
                    .setType(input.getMimeType())
                    .putExtra(Intent.EXTRA_STREAM, input.getMediaUri())
                    .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }

        @Override
        public Void parseResult(int resultCode, @Nullable Intent intent) {
            return null;
        }
    }
}
