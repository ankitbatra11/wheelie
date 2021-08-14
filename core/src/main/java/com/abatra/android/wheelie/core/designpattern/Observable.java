package com.abatra.android.wheelie.core.designpattern;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.function.Consumer;

public interface Observable<O> {

    static <O> Observable<O> copyOnWriteArraySet() {
        return new CollectionObservable<>(new CopyOnWriteArraySet<>());
    }

    static <O> Observable<O> synchronizedSet(Set<O> set) {
        return new CollectionObservable<>(Collections.synchronizedSet(set));
    }

    static <O> Observable<O> hashSet() {
        return new CollectionObservable<>(new HashSet<>());
    }

    void addObserver(O observer);

    void removeObserver(O observer);

    default void forEachObserver(Consumer<O> observerConsumer) {
    }

    default void removeObservers() {
    }
}
