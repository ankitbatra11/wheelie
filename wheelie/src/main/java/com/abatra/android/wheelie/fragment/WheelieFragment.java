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

    protected VB binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = createFragmentViewBinding();
        return Optional.ofNullable(binding)
                .map(ViewBinding::getRoot)
                .orElse(null);
    }

    @Nullable
    protected VB createFragmentViewBinding() {
        return null;
    }

    protected Optional<VB> getBinding() {
        return Optional.ofNullable(binding);
    }

    protected VB requireBinding() {
        return getBinding().orElseThrow(IllegalStateException::new);
    }
}
