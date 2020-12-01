package com.abatra.android.wheelie.pattern;

import com.abatra.android.wheelie.java8.Consumer;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

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

    void forEachObserver(Consumer<O> observerConsumer);
}
