package com.abatra.android.wheelie.lifecycle;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;

import com.abatra.android.wheelie.activity.ActivityResultRegistrar;

public interface ILifecycleOwner extends LifecycleOwner, ActivityResultRegistrar {

    default Context getContext() {
        Fragment fragment = getFragment();
        return fragment != null ? fragment.requireContext() : getActivity();
    }

    default Fragment getFragment() {
        return null;
    }

    default AppCompatActivity getActivity() {
        return getFragment() != null
                ? getFragment().getActivity() instanceof AppCompatActivity
                ? (AppCompatActivity) getFragment().getActivity()
                : null
                : null;
    }
}
