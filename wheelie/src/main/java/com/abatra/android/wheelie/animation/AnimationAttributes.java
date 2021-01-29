package com.abatra.android.wheelie.animation;

import android.os.Build;
import android.transition.Transition;
import android.view.View;

import java.util.Collection;
import java.util.HashSet;

public class AnimationAttributes {

    private final Collection<Integer> targetViewIds = new HashSet<>();
    private final Collection<View> targetViews = new HashSet<>();

    private AnimationAttributes() {
    }

    public static AnimationAttributes defaultAttributes() {
        return new AnimationAttributes();
    }

    public AnimationAttributes addTargetViewId(int targetViewId) {
        targetViewIds.add(targetViewId);
        return this;
    }

    public AnimationAttributes addTargetView(View targetView) {
        targetViews.add(targetView);
        return this;
    }

    public void set(Transition transition) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            for (Integer targetViewId : targetViewIds) {
                transition.addTarget(targetViewId);
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            for (View targetView : targetViews) {
                transition.addTarget(targetView);
            }
        }
    }

    public void set(androidx.transition.Transition transition) {
        for (Integer targetViewId : targetViewIds) {
            transition.addTarget(targetViewId);
        }
        for (View targetView : targetViews) {
            transition.addTarget(targetView);
        }
    }
}
