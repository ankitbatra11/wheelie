package com.abatra.android.wheelie.animation;

import android.app.Activity;
import android.os.Build;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.google.android.material.transition.platform.MaterialSharedAxis;

public class SharedAxisMotion implements MaterialMotion {

    public static final SharedAxisMotion X, Y, Z;

    static {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            X = new SharedAxisMotion(MaterialSharedAxis.X);
            Y = new SharedAxisMotion(MaterialSharedAxis.Y);
            Z = new SharedAxisMotion(MaterialSharedAxis.Z);
        } else {
            X = Y = Z = new SharedAxisMotion(0);
        }
    }

    private final int axis;

    private SharedAxisMotion(int axis) {
        this.axis = axis;
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
            activity.getWindow().setExitTransition(createPlatformMaterialSharedAxis(true, animationAttributes));
            activity.getWindow().setReenterTransition(createPlatformMaterialSharedAxis(false, animationAttributes));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private MaterialSharedAxis createPlatformMaterialSharedAxis(boolean forward, @Nullable AnimationAttributes animationAttributes) {
        MaterialSharedAxis materialSharedAxis = new MaterialSharedAxis(axis, forward);
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
            activity.getWindow().setEnterTransition(createPlatformMaterialSharedAxis(true, animationAttributes));
            activity.getWindow().setReturnTransition(createPlatformMaterialSharedAxis(false, animationAttributes));
            activity.getWindow().setAllowEnterTransitionOverlap(true);
        }
    }

    @Override
    public void setEnterAnimation(Fragment fragment) {
        fragment.setEnterTransition(createAndroidxMaterialSharedAxis(true));
        fragment.setReturnTransition(createAndroidxMaterialSharedAxis(false));
    }

    private com.google.android.material.transition.MaterialSharedAxis createAndroidxMaterialSharedAxis(boolean forward) {

        com.google.android.material.transition.MaterialSharedAxis materialSharedAxis =
                new com.google.android.material.transition.MaterialSharedAxis(axis, forward);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            materialSharedAxis.excludeTarget(android.R.id.statusBarBackground, true);
            materialSharedAxis.excludeTarget(android.R.id.navigationBarBackground, true);
        }
        return materialSharedAxis;
    }

    @Override
    public void setExitAnimation(Fragment fragment) {
        fragment.setExitTransition(createAndroidxMaterialSharedAxis(true));
        fragment.setReenterTransition(createAndroidxMaterialSharedAxis(false));
    }
}
