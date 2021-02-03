package com.abatra.android.wheelie.activity;

import android.content.Intent;

import androidx.fragment.app.Fragment;

public interface ActivityStarter {

    static ActivityStarter from(Fragment fragment) {
        return new FragmentActivityStarter(fragment);
    }

    void startActivity(Intent intent);
}
