package com.abatra.android.wheelie.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewbinding.ViewBinding;

import com.abatra.android.wheelie.lifecycle.owner.ILifecycleOwner;
import com.abatra.android.wheelie.lifecycle.LifecycleBoundValue;

import java.util.function.BiFunction;

public class FragmentViewBinding<VB extends ViewBinding> extends LifecycleBoundValue<VB> implements FragmentViewCreator {

    private final BiFunction<LayoutInflater, ViewGroup, VB> bindingInflater;

    public FragmentViewBinding(BiFunction<LayoutInflater, ViewGroup, VB> bindingInflater) {
        this.bindingInflater = bindingInflater;
    }

    @Nullable
    @Override
    public View createView(Fragment fragment, LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState) {
        setValue(bindingInflater.apply(layoutInflater, container));
        observeLifecycle(ILifecycleOwner.viewOf(fragment));
        return getValue().map(ViewBinding::getRoot).orElse(null);
    }
}
