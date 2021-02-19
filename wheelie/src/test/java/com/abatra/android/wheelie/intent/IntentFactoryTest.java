package com.abatra.android.wheelie.intent;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = Build.VERSION_CODES.P)
public class IntentFactoryTest {

    public static final String EXP_PACKAGE_PREFIX = "package:";

    @Test
    public void testOpenAppDetailsSettings() {

        Intent intent = IntentFactory.openAppDetailsSettings(getApplicationContext());

        assertThat(intent.getAction(), equalTo(Settings.ACTION_APPLICATION_DETAILS_SETTINGS));
        assertThat(intent.getData(), equalTo(Uri.parse(EXP_PACKAGE_PREFIX + getApplicationContext().getPackageName())));
    }
}
