package com.abatra.android.wheelie.resource;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.widget.TextView;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SpannableStringBuilderTextTest {

    @InjectMocks
    private SpannableStringBuilderText spannableStringBuilderText;

    @Mock
    private SpannableStringBuilder mockedSpannableStringBuilder;

    @Test
    public void test_show() {

        TextView mockedTextView = mock(TextView.class);

        spannableStringBuilderText.show(mockedTextView);

        verify(mockedTextView, times(1)).setText(mockedSpannableStringBuilder);
    }

    @Test
    public void test_getString() {

        Context mockedContext = mock(Context.class);
        when(mockedSpannableStringBuilder.toString()).thenReturn("hey");

        assertThat(spannableStringBuilderText.getString(mockedContext), equalTo("hey"));

        verifyNoInteractions(mockedContext);
    }
}
