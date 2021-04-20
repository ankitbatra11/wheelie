package com.abatra.android.wheelie.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewbinding.ViewBinding;

import java.util.Optional;

public class WheelieFragment<VB extends ViewBinding> extends Fragment {

    private FragmentViewBinding<VB> fragmentViewBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        fragmentViewBinding = createFragmentViewBinding();
        return Optional.ofNullable(fragmentViewBinding)
                .map(b -> b.createView(this, inflater, container, savedInstanceState))
                .orElse(null);
    }

    @Nullable
    protected FragmentViewBinding<VB> createFragmentViewBinding() {
        return null;
    }

    protected VB requireBinding() {
        return fragmentViewBinding.requireValue();
    }

    protected Optional<VB> getBinding() {
        return fragmentViewBinding.getValue();
    }
}
