package com.abatra.android.wheelie.startup;

import com.google.firebase.crashlytics.FirebaseCrashlytics;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static android.util.Log.ERROR;
import static android.util.Log.INFO;
import static android.util.Log.WARN;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(MockitoJUnitRunner.class)
public class CrashlyticsTimberTreeTest {

    public static final String TAG = "tag";
    public static final String MESSAGE = "message";

    @InjectMocks
    private CrashlyticsTimberTree crashlyticsTimberTree;

    @Mock
    private FirebaseCrashlytics mockedFirebaseCrashlytics;

    @Captor
    private ArgumentCaptor<String> logMessageCaptor;

    @Test
    public void test_log_belowWarnLevel() {

        crashlyticsTimberTree.log(INFO, TAG, MESSAGE, null);

        verifyNoInteractions(mockedFirebaseCrashlytics);
    }

    @Test
    public void test_log_warnLevel() {

        crashlyticsTimberTree.log(WARN, TAG, MESSAGE, null);

        verify(mockedFirebaseCrashlytics, times(1)).log(logMessageCaptor.capture());
        assertThat(logMessageCaptor.getValue(), equalTo(getExpectedLogMessage(WARN)));
        verifyNoMoreInteractions(mockedFirebaseCrashlytics);
    }

    private String getExpectedLogMessage(int level) {
        return level + "/" + TAG + ": " + MESSAGE;
    }

    @Test
    public void test_log_errorLevel() {

        crashlyticsTimberTree.log(ERROR, TAG, MESSAGE, null);

        verify(mockedFirebaseCrashlytics, times(1)).log(logMessageCaptor.capture());
        assertThat(logMessageCaptor.getValue(), equalTo(getExpectedLogMessage(ERROR)));
        verifyNoMoreInteractions(mockedFirebaseCrashlytics);
    }

    @Test
    public void test_log_errorLevel_withError() {

        RuntimeException runtimeException = new RuntimeException();
        crashlyticsTimberTree.log(ERROR, TAG, MESSAGE, runtimeException);

        verify(mockedFirebaseCrashlytics, times(1)).log(logMessageCaptor.capture());
        assertThat(logMessageCaptor.getValue(), equalTo(getExpectedLogMessage(ERROR)));

        verify(mockedFirebaseCrashlytics, times(1)).recordException(runtimeException);
    }
}
