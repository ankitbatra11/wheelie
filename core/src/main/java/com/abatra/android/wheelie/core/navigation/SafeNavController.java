package com.abatra.android.wheelie.core.navigation;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.Navigator;
import androidx.navigation.fragment.FragmentNavigator;
import androidx.navigation.fragment.NavHostFragment;

import com.abatra.android.wheelie.core.async.bolts.SaferTask;

import timber.log.Timber;

public class SafeNavController extends NavController {

    private final NavController navController;

    private SafeNavController(Context context, NavController navController) {
        super(context);
        this.navController = navController;
    }

    public static SafeNavController findNavController(Fragment fragment) {
        return new SafeNavController(fragment.requireContext(), NavHostFragment.findNavController(fragment));
    }

    public static SafeNavController findNavController(View view) {
        return new SafeNavController(view.getContext(), Navigation.findNavController(view));
    }

    @Override
    public void navigate(int resId) {
        try {
            navController.navigate(resId);
        } catch (IllegalArgumentException e) {
            Timber.e(e);
        }
    }

    @Override
    public void navigate(int resId, @Nullable Bundle args) {
        try {
            navController.navigate(resId, args);
        } catch (IllegalArgumentException e) {
            Timber.e(e);
        }
    }

    @Override
    public void navigate(int resId, @Nullable Bundle args, @Nullable NavOptions navOptions, @Nullable Navigator.Extras navigatorExtras) {
        try {
            navController.navigate(resId, args, navOptions, navigatorExtras);
        } catch (Exception e) {
            Timber.e(e);
        }
    }

    @Override
    public boolean popBackStack() {
        return navController.popBackStack();
    }

    @Nullable
    @Override
    @SuppressWarnings("NullableProblems")
    public NavBackStackEntry getBackStackEntry(int destinationId) {
        NavBackStackEntry navBackStackEntry = null;
        try {
            navBackStackEntry = navController.getBackStackEntry(destinationId);
        } catch (RuntimeException e) {
            Timber.e(e);
        }
        return navBackStackEntry;
    }

    public void navigate(int action, FragmentNavigator.Extras extras) {
        SaferTask.uiTask(() -> {
            navigate(action, null, null, extras);
            return null;
        });
    }
}
