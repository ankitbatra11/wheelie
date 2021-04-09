package com.abatra.android.wheelie.lifecycle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;

public class FragmentViewLifecycleOwner implements ILifecycleOwner {

    private final Fragment fragment;

    public FragmentViewLifecycleOwner(Fragment fragment) {
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return fragment.getViewLifecycleOwner().getLifecycle();
    }

    @Override
    public Fragment getFragment() {
        return fragment;
    }
}
