package com.abatra.android.wheelie.firebase.remoteConfig;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.sameInstance;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;

import android.os.Build;

import com.google.firebase.FirebaseApp;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockedStatic;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = Build.VERSION_CODES.P)
public class FirebaseDynamicConfigTest {

    @Test
    public void test_getInstance_firstCallReturnsNewInstance() {
        try (MockedStatic<FirebaseApp> firebaseAppMockedStatic = mockStatic(FirebaseApp.class)) {
            try (MockedStatic<FirebaseRemoteConfig> firebaseRemoteConfigMockedStatic = mockStatic(FirebaseRemoteConfig.class)) {
                FirebaseDynamicConfig.instance = null;

                assertThat(FirebaseDynamicConfig.getInstance(getApplicationContext()), sameInstance(FirebaseDynamicConfig.instance));

                firebaseRemoteConfigMockedStatic.verify(FirebaseRemoteConfig::getInstance, times(1));
                firebaseAppMockedStatic.verify(() -> FirebaseApp.initializeApp(getApplicationContext()), times(1));
            }
        }
    }

    @Test
    @SuppressWarnings("unused")
    public void test_getInstance_secondCallReturnsSameInstance() {
        try (MockedStatic<FirebaseApp> firebaseAppMockedStatic = mockStatic(FirebaseApp.class)) {
            try (MockedStatic<FirebaseRemoteConfig> firebaseRemoteConfigMockedStatic = mockStatic(FirebaseRemoteConfig.class)) {
                FirebaseDynamicConfig config = FirebaseDynamicConfig.getInstance(getApplicationContext());
                assertThat(FirebaseDynamicConfig.getInstance(getApplicationContext()), sameInstance(config));
            }
        }
    }
}
