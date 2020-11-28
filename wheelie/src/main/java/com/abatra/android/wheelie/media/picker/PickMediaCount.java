package com.abatra.android.wheelie.media.picker;

import android.net.Uri;

import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;

import java.util.List;

public enum PickMediaCount {
    MULTIPLE {
        @Override
        public ActivityResultContract<String, List<Uri>> getActivityResultContract() {
            return new ActivityResultContracts.GetMultipleContents();
        }
    };

    public abstract ActivityResultContract<String, List<Uri>> getActivityResultContract();
}
