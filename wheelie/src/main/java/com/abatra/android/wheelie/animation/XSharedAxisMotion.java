package com.abatra.android.wheelie.animation;

import android.app.Activity;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.android.material.transition.platform.MaterialSharedAxis;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class XSharedAxisMotion implements SharedAxisMotion {

    private static final XSharedAxisMotion instance = new XSharedAxisMotion();

    private XSharedAxisMotion() {
    }

    static XSharedAxisMotion getInstance() {
        return instance;
    }

    @Override
    public void applyExitAnimation(Activity activity, AnimationAttributes animationAttributes) {
        activity.getWindow().setExitTransition(createMaterialSharedAxis(true, animationAttributes));
        activity.getWindow().setReenterTransition(createMaterialSharedAxis(false, animationAttributes));
    }

    private MaterialSharedAxis createMaterialSharedAxis(boolean forward, AnimationAttributes animationAttributes) {
        MaterialSharedAxis materialSharedAxis = new MaterialSharedAxis(MaterialSharedAxis.X, forward);
        materialSharedAxis.excludeTarget(android.R.id.statusBarBackground, true);
        materialSharedAxis.excludeTarget(android.R.id.navigationBarBackground, true);
        animationAttributes.set(materialSharedAxis);
        return materialSharedAxis;
    }

    @Override
    public void applyEnterAnimation(Activity activity, AnimationAttributes animationAttributes) {
        activity.getWindow().setEnterTransition(createMaterialSharedAxis(true, animationAttributes));
        activity.getWindow().setReturnTransition(createMaterialSharedAxis(false, animationAttributes));
        activity.getWindow().setAllowEnterTransitionOverlap(true);
    }
}
