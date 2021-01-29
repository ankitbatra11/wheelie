package com.abatra.android.wheelie.animation;

import android.app.Activity;
import android.os.Build;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.google.android.material.transition.platform.MaterialSharedAxis;

public class SharedAxisMotion implements MaterialMotion {

    public static final SharedAxisMotion X = new SharedAxisMotion();

    private SharedAxisMotion() {
    }

    @Override
    public void setExitAnimation(Activity activity) {
        setExitAnimation(activity, null);
    }

    @Override
    public void setEnterAnimation(Activity activity) {
        setEnterAnimation(activity, null);
    }

    @Override
    public void setExitAnimation(Activity activity, @Nullable AnimationAttributes animationAttributes) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().setExitTransition(createMaterialSharedAxis(true, animationAttributes));
            activity.getWindow().setReenterTransition(createMaterialSharedAxis(false, animationAttributes));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private MaterialSharedAxis createMaterialSharedAxis(boolean forward, @Nullable AnimationAttributes animationAttributes) {
        MaterialSharedAxis materialSharedAxis = new MaterialSharedAxis(MaterialSharedAxis.X, forward);
        materialSharedAxis.excludeTarget(android.R.id.statusBarBackground, true);
        materialSharedAxis.excludeTarget(android.R.id.navigationBarBackground, true);
        if (animationAttributes != null) {
            animationAttributes.set(materialSharedAxis);
        }
        return materialSharedAxis;
    }

    @Override
    public void setEnterAnimation(Activity activity, AnimationAttributes animationAttributes) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().setEnterTransition(createMaterialSharedAxis(true, animationAttributes));
            activity.getWindow().setReturnTransition(createMaterialSharedAxis(false, animationAttributes));
            activity.getWindow().setAllowEnterTransitionOverlap(true);
        }
    }
}
