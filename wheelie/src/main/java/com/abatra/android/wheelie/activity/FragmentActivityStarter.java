package com.abatra.android.wheelie.activity;

import android.content.Intent;

import androidx.fragment.app.Fragment;

public class FragmentActivityStarter implements ActivityStarter {

    private final Fragment fragment;

    public FragmentActivityStarter(Fragment fragment) {
        this.fragment = fragment;
    }

    @Override
    public void startActivity(Intent intent) {
        fragment.startActivity(intent);
    }
}
