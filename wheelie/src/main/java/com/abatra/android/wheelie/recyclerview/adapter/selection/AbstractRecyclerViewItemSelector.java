package com.abatra.android.wheelie.recyclerview.adapter.selection;

import com.abatra.android.wheelie.java8.Consumer;
import com.abatra.android.wheelie.pattern.Observable;

abstract public class AbstractRecyclerViewItemSelector implements RecyclerViewItemSelector {

    private final Observable<Listener> observers = Observable.hashSet();

    @Override
    public void addObserver(Listener observer) {
        observers.addObserver(observer);
    }

    @Override
    public void removeObserver(Listener observer) {
        observers.removeObserver(observer);
    }

    @Override
    public void forEachObserver(Consumer<Listener> observerConsumer) {
        observers.forEachObserver(observerConsumer);
    }

    @Override
    public void removeObservers() {
        observers.removeObservers();
    }
}
