package com.abatra.android.wheelie.pattern;

import com.abatra.android.wheelie.java8.Consumer;

import java.util.concurrent.CopyOnWriteArraySet;

public interface Observable<O> {

    static <O> Observable<O> copyOnWriteArraySet() {
        return new CollectionObservable<>(new CopyOnWriteArraySet<>());
    }

    void addObserver(O observer);

    void removeObserver(O observer);

    void forEachObserver(Consumer<O> observerConsumer);
}
