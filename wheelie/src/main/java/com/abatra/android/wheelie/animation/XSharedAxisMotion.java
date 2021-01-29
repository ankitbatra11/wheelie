package com.abatra.android.wheelie.animation;

import android.app.Activity;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.android.material.transition.platform.MaterialSharedAxis;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class XSharedAxisMotion implements SharedAxisMotion {

    public static XSharedAxisMotion INSTANCE = new XSharedAxisMotion();

    private XSharedAxisMotion() {
    }

    @Override
    public void applyExitAnimation(Activity activity) {
        activity.getWindow().setExitTransition(new MaterialSharedAxis(MaterialSharedAxis.X, true));
    }

    @Override
    public void applyEnterAnimation(Activity activity) {
        activity.getWindow().setEnterTransition(new MaterialSharedAxis(MaterialSharedAxis.X, true));
        activity.getWindow().setAllowEnterTransitionOverlap(true);
    }
}
