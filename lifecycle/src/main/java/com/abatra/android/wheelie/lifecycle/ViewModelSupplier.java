package com.abatra.android.wheelie.lifecycle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

public class ViewModelSupplier {

    public <T extends ViewModel> T getViewModel(ViewModelStoreOwner viewModelStoreOwner, Class<T> tClass) {
        return new ViewModelProvider(viewModelStoreOwner).get(tClass);
    }

    public <T extends ViewModel> T getActivityViewModel(Fragment fragment, Class<T> tClass) {
        return getViewModel(fragment.requireActivity(), tClass);
    }

    public <T extends ViewModel> T getParentFragmentViewModel(Fragment fragment, Class<T> tClass) {
        return getViewModel(fragment.requireParentFragment(), tClass);
    }
}
