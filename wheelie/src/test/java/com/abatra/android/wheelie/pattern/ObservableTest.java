package com.abatra.android.wheelie.pattern;

import org.junit.Test;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.function.Supplier;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;

public class ObservableTest {

    @Test
    public void testCopyOnWriteArraySet() {
        testObservableFactory(Observable::copyOnWriteArraySet, CopyOnWriteArraySet.class);
    }

    @SuppressWarnings("rawtypes")
    private void testObservableFactory(Supplier<Observable> observableSupplier, Class expectedClass) {
        Observable observable = observableSupplier.get();
        assertThat(observable, instanceOf(CollectionObservable.class));
        CollectionObservable collectionObservable = (CollectionObservable) observable;
        assertThat(collectionObservable.getCollection(), instanceOf(expectedClass));
    }

    @Test
    public void testSynchronizedSet() {
        testObservableFactory(() -> Observable.synchronizedSet(new HashSet<>()), Set.class);
    }

    @Test
    public void testHashSet() {
        testObservableFactory(Observable::hashSet, HashSet.class);
    }
}
