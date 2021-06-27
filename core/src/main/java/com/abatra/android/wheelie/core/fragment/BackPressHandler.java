package com.abatra.android.wheelie.core.fragment;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import com.abatra.android.wheelie.core.navigation.SafeNavController;

public class BackPressHandler implements LifecycleObserver {

    private Fragment fragment;

    public BackPressHandler(Fragment fragment) {
        this.fragment = fragment;
    }

    public static void popBackStackOnBackPress(Fragment fragment) {
        fragment.getViewLifecycleOwner().getLifecycle().addObserver(new BackPressHandler(fragment));
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    public void onCreate() {
        fragment.requireActivity().getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                SafeNavController.findNavController(fragment).popBackStack();
            }
        });
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onDestroy() {
        fragment = null;
    }

}
