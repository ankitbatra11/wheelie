package com.abatra.android.wheelie.yara.selection.actionmode;

import android.os.Build;
import android.view.ActionMode;
import android.view.Menu;
import android.view.Window;

public class ActionModeStatusBar implements SelectionActionModeCallback {

    private final Window window;
    private final int statusBarColor;
    private int outOfActionModeStatusBarColor;

    public ActionModeStatusBar(Window window, int statusBarColor) {
        this.window = window;
        this.statusBarColor = statusBarColor;
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            outOfActionModeStatusBarColor = window.getStatusBarColor();
            window.setStatusBarColor(statusBarColor);
        }
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(outOfActionModeStatusBarColor);
        }
    }
}
