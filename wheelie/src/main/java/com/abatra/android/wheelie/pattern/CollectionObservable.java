package com.abatra.android.wheelie.pattern;

import com.abatra.android.wheelie.java8.Consumer;

import java.util.Collection;

import timber.log.Timber;

public class CollectionObservable<O> implements Observable<O> {

    private final Collection<O> collection;

    public CollectionObservable(Collection<O> collection) {
        this.collection = collection;
    }

    @Override
    public void addObserver(O observer) {
        collection.add(observer);
    }

    @Override
    public void removeObserver(O observer) {
        collection.remove(observer);
    }

    @Override
    public void forEachObserver(Consumer<O> observerConsumer) {
        for (O o : collection) {
            try {
                observerConsumer.accept(o);
            } catch (Throwable t) {
                Timber.e(t);
            }
        }
    }
}
