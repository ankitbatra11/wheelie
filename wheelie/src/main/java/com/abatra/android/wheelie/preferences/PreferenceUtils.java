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
                Preference currentPreference = ((PreferenceGroup) preference).getPreference(i);
                if (currentPreference instanceof PreferenceGroup) {
                    tintIcons(preference, color);
                } else {
                    tintSinglePreference(preference, color);
                }
            }
        } else {
            tintSinglePreference(preference, color);
        }
    }

    private static void tintSinglePreference(Preference preference, int color) {
        preference.setIcon(createTintedDrawable(preference.getIcon(), color));
    }

    @Nullable
    private static Drawable createTintedDrawable(@Nullable Drawable drawable, int color) {
        Optional<Drawable> drawableOptional = Optional.ofNullable(drawable).map(DrawableCompat::wrap);
        drawableOptional.ifPresent(d -> DrawableCompat.setTint(d, color));
        return drawableOptional.orElse(null);
    }
}
