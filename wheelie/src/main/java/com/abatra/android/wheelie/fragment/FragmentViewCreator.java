package com.abatra.android.wheelie.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public interface FragmentViewCreator {
    @Nullable
    View createView(Fragment fragment, LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState);
}
