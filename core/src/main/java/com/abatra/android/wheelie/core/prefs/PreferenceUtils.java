package com.abatra.android.wheelie.core.prefs;

import androidx.preference.Preference;
import androidx.preference.PreferenceGroup;

import com.abatra.android.wheelie.core.res.image.Image;

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
        Optional.ofNullable(preference.getIcon())
                .map(Image::drawable)
                .map(image -> image.tint(color))
                .map(image -> image.getDrawable(preference.getContext()))
                .ifPresent(preference::setIcon);
    }
}
