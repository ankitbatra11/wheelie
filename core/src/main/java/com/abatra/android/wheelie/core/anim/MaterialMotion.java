package com.abatra.android.wheelie.core.anim;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public interface MaterialMotion {

    void setExitAnimation(Activity activity);

    void setEnterAnimation(Activity activity);

    void setExitAnimation(Activity activity, @Nullable AnimationAttributes animationAttributes);

    void setEnterAnimation(Activity activity, @Nullable AnimationAttributes animationAttributes);

    void setEnterAnimation(Fragment fragment);

    void setExitAnimation(Fragment fragment);

    default void startActivityWithAnimation(Intent intent, Activity activity) {
        Bundle options = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            options = ActivityOptions.makeSceneTransitionAnimation(activity).toBundle();
        }
        activity.startActivity(intent, options);
    }
}
