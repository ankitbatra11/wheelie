package com.abatra.android.wheelie.animation;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;

public interface MaterialMotion {

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    static MaterialMotion sharedXAxis() {
        return XSharedAxisMotion.getInstance();
    }

    void applyExitAnimation(Activity activity, AnimationAttributes animationAttributes);

    void applyEnterAnimation(Activity activity, AnimationAttributes animationAttributes);

    default void startActivityWithAnimation(Intent intent, Activity activity) {
        Bundle options = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            options = ActivityOptions.makeSceneTransitionAnimation(activity).toBundle();
        }
        activity.startActivity(intent, options);
    }
}
