package com.abatra.android.wheelie.logger;

import android.os.Build;

import com.abatra.android.wheelie.logger.CrashlyticsTimberTree;
import com.abatra.android.wheelie.logger.TimberInitializer;
import com.google.firebase.crashlytics.FirebaseCrashlytics;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import timber.log.Timber;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.instanceOf;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = Build.VERSION_CODES.P)
public class TimberInitializerTest {

    private final TimberInitializer debugTimberInitializer = new TimberInitializer() {
        @Override
        protected boolean isDebug() {
            return true;
        }
    };
    private final TimberInitializer releaseTimberInitializer = new TimberInitializer() {
        @Override
        protected boolean isDebug() {
            return false;
        }
    };

    @Captor
    private ArgumentCaptor<Timber.Tree> treeArgumentCaptor;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testDebugCreate() {
        try (MockedStatic<Timber> timberMockedStatic = mockStatic(Timber.class)) {
            debugTimberInitializer.create(getApplicationContext());
            timberMockedStatic.verify(() ->
            {
                Timber.plant(treeArgumentCaptor.capture());
                assertThat(treeArgumentCaptor.getAllValues(), hasSize(1));
                assertThat(treeArgumentCaptor.getValue(), instanceOf(Timber.DebugTree.class));

            }, times(1));
        }
    }

    @Test
    public void testReleaseCreate() {
        try (MockedStatic<Timber> timberMockedStatic = mockStatic(Timber.class)) {
            //noinspection unused create a mock so that FirebaseCrashlytics.getInstance does not throw any error for tests
            try (MockedStatic<FirebaseCrashlytics> crashlyticsMockedStatic = mockStatic(FirebaseCrashlytics.class)) {
                releaseTimberInitializer.create(getApplicationContext());
                timberMockedStatic.verify(() ->
                {
                    Timber.plant(treeArgumentCaptor.capture());
                    assertThat(treeArgumentCaptor.getAllValues(), hasSize(1));
                    assertThat(treeArgumentCaptor.getValue(), instanceOf(CrashlyticsTimberTree.class));

                }, times(1));
            }
        }
    }

    @Test
    public void testDependencies() {
        assertThat(debugTimberInitializer.dependencies(), hasSize(0));
        assertThat(releaseTimberInitializer.dependencies(), hasSize(0));
    }
}
