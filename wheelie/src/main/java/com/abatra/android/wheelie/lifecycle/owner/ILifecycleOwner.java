package com.abatra.android.wheelie.lifecycle.owner;

import android.content.Context;

import androidx.activity.result.ActivityResultCaller;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;

import com.abatra.android.wheelie.activity.ActivityStarter;

public interface ILifecycleOwner extends LifecycleOwner, ActivityResultCaller {

    static ILifecycleOwner viewOf(Fragment fragment) {
        return new FragmentViewLifecycleOwner(fragment);
    }

    static ILifecycleOwner fragment(Fragment fragment) {
        return new FragmentLifecycleOwner(fragment);
    }

    static ILifecycleOwner activity(AppCompatActivity activity) {
        return new AppCompatActivityLifecycleOwner(activity);
    }

    Context getContext();

    default Fragment getFragment() {
        return null;
    }

    default AppCompatActivity getAppCompatActivity() {
        return null;
    }
}
