package com.abatra.android.wheelie.yara.selection;

import com.abatra.android.wheelie.designPattern.Observable;

import java.util.function.Consumer;

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
