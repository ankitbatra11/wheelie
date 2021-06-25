package com.abatra.android.wheelie.core.content;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import com.abatra.android.wheelie.core.content.Intents;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = Build.VERSION_CODES.P)
public class IntentsTest {

    private static final String EXP_PACKAGE_PREFIX = "package:";

    @Test
    public void testOpenAppDetailsSettings() {

        Intent intent = Intents.openAppDetailsSettings(getApplicationContext());

        assertThat(intent.getAction(), equalTo(Settings.ACTION_APPLICATION_DETAILS_SETTINGS));
        assertThat(intent.getData(), equalTo(Uri.parse(EXP_PACKAGE_PREFIX + getApplicationContext().getPackageName())));
    }

    @Test
    public void testManageOverlayPermission() {

        Intent intent = Intents.manageOverlayPermission(getApplicationContext());

        assertThat(intent.getAction(), equalTo(Settings.ACTION_MANAGE_OVERLAY_PERMISSION));
        assertThat(intent.getData(), equalTo(Uri.parse(EXP_PACKAGE_PREFIX + getApplicationContext().getPackageName())));
    }
}
