package com.abatra.android.wheelie.lifecycle.owner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;

public class FragmentViewLifecycleOwner extends FragmentLifecycleOwner {

    public FragmentViewLifecycleOwner(Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return getFragment().getViewLifecycleOwner().getLifecycle();
    }
}
