package com.abatra.android.wheelie.yara;

import androidx.recyclerview.widget.RecyclerView;

import com.abatra.android.wheelie.core.designpattern.Observable;

import java.util.function.Consumer;

public class CompositeDataObserver extends RecyclerView.AdapterDataObserver implements Observable<RecyclerView.AdapterDataObserver> {

    private final Observable<RecyclerView.AdapterDataObserver> observers = Observable.hashSet();

    @Override
    public void addObserver(RecyclerView.AdapterDataObserver observer) {
        observers.addObserver(observer);
    }

    @Override
    public void removeObserver(RecyclerView.AdapterDataObserver observer) {
        observers.removeObserver(observer);
    }

    @Override
    public void forEachObserver(Consumer<RecyclerView.AdapterDataObserver> observerConsumer) {
        observers.forEachObserver(observerConsumer);
    }

    @Override
    public void removeObservers() {
        observers.removeObservers();
    }

    @Override
    public void onChanged() {
        super.onChanged();
        forEachObserver(RecyclerView.AdapterDataObserver::onChanged);
    }
}
