package com.abatra.android.wheelie.core.content;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.TaskStackBuilder;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PendingIntentBuilderTest {

    @Mock
    private Context mockedContext;

    @Mock
    private Intent mockedIntent;

    @InjectMocks
    private PendingIntentBuilder pendingIntentBuilder;

    @Mock
    private TaskStackBuilder mockedTaskStackBuilder;

    @Test
    public void test_cancelCurrent() {

        assertThat(pendingIntentBuilder.pendingIntentFlags, equalTo(PendingIntent.FLAG_UPDATE_CURRENT));

        pendingIntentBuilder.cancelCurrent();

        assertThat(pendingIntentBuilder.pendingIntentFlags, equalTo(PendingIntent.FLAG_CANCEL_CURRENT));
    }

    @Test
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void test_buildParentStackPendingIntent() {
        try (MockedStatic<TaskStackBuilder> taskStackBuilderMockedStatic = mockStatic(TaskStackBuilder.class)) {

            taskStackBuilderMockedStatic.when(() -> TaskStackBuilder.create(mockedContext)).thenReturn(mockedTaskStackBuilder);

            pendingIntentBuilder.buildParentStackPendingIntent();

            taskStackBuilderMockedStatic.verify(() -> TaskStackBuilder.create(mockedContext), times(1));
            verify(mockedTaskStackBuilder, times(1)).addNextIntentWithParentStack(mockedIntent);
            verify(mockedTaskStackBuilder, times(1)).getPendingIntent(0, pendingIntentBuilder.pendingIntentFlags);
        }
    }

    @Test
    public void test_buildSpecialActivityPendingIntent() {
        try (MockedStatic<PendingIntent> pendingIntentMockedStatic = mockStatic(PendingIntent.class)) {

            when(mockedIntent.addFlags(anyInt())).thenReturn(mockedIntent);

            pendingIntentBuilder.buildSpecialActivityPendingIntent();

            //noinspection CodeBlock2Expr
            pendingIntentMockedStatic.verify(() -> {

                PendingIntent.getActivity(mockedContext,
                        0,
                        mockedIntent,
                        pendingIntentBuilder.pendingIntentFlags);

            }, times(1));

            verify(mockedIntent, times(1)).addFlags(eq(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }
}
