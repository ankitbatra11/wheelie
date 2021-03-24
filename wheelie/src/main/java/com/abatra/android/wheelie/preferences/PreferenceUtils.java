package com.abatra.android.wheelie.preferences;

import android.graphics.drawable.Drawable;

import androidx.annotation.Nullable;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.preference.Preference;
import androidx.preference.PreferenceGroup;

import java.util.Optional;

public class PreferenceUtils {

    private PreferenceUtils() {
    }

    public static void tintIcons(Preference preference, int color) {
        if (preference instanceof PreferenceGroup) {
            for (int i = 0; i < ((PreferenceGroup) preference).getPreferenceCount(); i++) {
                tintIcons(((PreferenceGroup) preference).getPreference(i), color);
            }
        } else {
            tintIcon(preference, color);
        }
    }

    private static void tintIcon(Preference preference, int color) {
        preference.setIcon(createTintedDrawable(preference.getIcon(), color));
    }

    @Nullable
    private static Drawable createTintedDrawable(@Nullable Drawable drawable, int color) {
        return Optional.ofNullable(drawable)
                .map(DrawableCompat::wrap)
                .map(mutableDrawable -> {
                    DrawableCompat.setTint(mutableDrawable, color);
                    return mutableDrawable;
                })
                .orElse(null);
    }
}
