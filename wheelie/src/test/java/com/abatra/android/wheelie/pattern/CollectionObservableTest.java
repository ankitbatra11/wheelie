package com.abatra.android.wheelie.pattern;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@RunWith(MockitoJUnitRunner.class)
public class CollectionObservableTest {

    private CollectionObservable<Runnable> collectionObservable;

    @Mock
    private Runnable mockedRunnable;

    @Before
    public void setup() {
        collectionObservable = new CollectionObservable<>(new ArrayList<>());
    }

    @Test
    public void testAddObserver() {

        collectionObservable.addObserver(mockedRunnable);
        collectionObservable.forEachObserver(Runnable::run);

        verify(mockedRunnable, times(1)).run();
    }

    @Test
    public void testRemoveObserver() {

        collectionObservable.addObserver(mockedRunnable);
        collectionObservable.removeObserver(mockedRunnable);

        collectionObservable.forEachObserver(Runnable::run);

        verifyNoInteractions(mockedRunnable);
    }

    @Test
    public void testForEachObserver() {

        Runnable mockedThrowingRunnable = mock(Runnable.class);
        doThrow(new RuntimeException()).when(mockedThrowingRunnable).run();
        collectionObservable.addObserver(mockedThrowingRunnable);
        collectionObservable.addObserver(mockedRunnable);

        collectionObservable.forEachObserver(Runnable::run);

        verify(mockedRunnable, times(1)).run();
    }

    @Test
    public void testRemoveObservers() {

        collectionObservable.addObserver(mockedRunnable);
        collectionObservable.addObserver(mockedRunnable);
        collectionObservable.removeObservers();

        collectionObservable.forEachObserver(Runnable::run);

        verifyNoInteractions(mockedRunnable);
    }
}
