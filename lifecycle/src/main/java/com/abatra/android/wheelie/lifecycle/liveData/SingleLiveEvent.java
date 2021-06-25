package com.abatra.android.wheelie.lifecycle.liveData;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import timber.log.Timber;

/**
 * A lifecycle-aware observable that sends only new updates after subscription, used for events like
 * navigation and Snackbar messages.
 * <p>
 * This avoids a common problem with events: on configuration change (like rotation) an update
 * can be emitted if the observer is active. This LiveData only calls the observable if there's an
 * explicit call to setValue() or call().
 * <p>
 * Note that only one observer is going to be notified of changes.
 */
public class SingleLiveEvent<T> extends MutableLiveData<T> {

    private final AtomicBoolean mPending = new AtomicBoolean(false);

    private Class<?> ownerClass;

    public SingleLiveEvent(Class<?> ownerClass) {
        this.ownerClass = ownerClass;
    }

    public SingleLiveEvent() {
        this(null);
    }

    @Override
    public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<? super T> observer) {

        if (hasActiveObservers()) {
            Timber.i("Multiple observers registered but only one will be notified of changes.");
        }

        boolean notify = Optional.ofNullable(ownerClass)
                .map(nonNullOwnerClass -> owner.getClass().equals(nonNullOwnerClass))
                .orElse(true);

        if (notify) {
            // Observe the internal MutableLiveData
            super.observe(owner, t -> {
                if (mPending.compareAndSet(true, false)) {
                    observer.onChanged(t);
                }
            });
        }
    }

    @MainThread
    public void setValue(@Nullable T t) {
        mPending.set(true);
        super.setValue(t);
    }

    /**
     * Used for cases where T is Void, to setup calls cleaner.
     */
    @MainThread
    @SuppressWarnings("unused")
    public void call() {
        setValue(null);
    }
}