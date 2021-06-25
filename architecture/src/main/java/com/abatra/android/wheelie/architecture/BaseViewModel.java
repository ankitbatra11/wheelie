package com.abatra.android.wheelie.architecture;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.abatra.android.wheelie.lifecycle.liveData.SingleLiveEvent;

import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

import bolts.Task;
import timber.log.Timber;

import static com.abatra.android.wheelie.core.async.bolts.SaferTask.uiTask;
import static java.util.Objects.requireNonNull;

public class BaseViewModel<VS, V extends IView<VS>> extends ViewModel {

    protected final MutableLiveData<VS> viewState = createViewStateLiveData();
    protected final MutableLiveData<ViewEffect<VS, V>> viewEffect = createViewEffectLiveData();
    protected Executor backgroundExecutor = Task.BACKGROUND_EXECUTOR;

    protected MutableLiveData<ViewEffect<VS, V>> createViewEffectLiveData() {
        return new SingleLiveEvent<>();
    }

    protected MutableLiveData<VS> createViewStateLiveData() {
        return new MutableLiveData<>();
    }

    public void addObserver(LifecycleOwner lifecycleOwner, V view) {
        viewState.observe(lifecycleOwner, viewStateResource -> {
            Timber.v("viewStateResource=%s", viewStateResource);
            view.update(viewStateResource);
        });
        viewEffect.observe(lifecycleOwner, ve -> ve.update(view));
    }

    public <E extends Event> void process(E event) {
        Timber.d("event=%s", event);
        //noinspection SwitchStatementWithTooFewBranches
        switch (event.getType()) {
            case Event.TYPE_LOAD_VIEW_STATE:
                loadViewStateIfRequired(event);
                break;
        }
    }

    protected void loadViewStateIfRequired(Event event) {
        if (getViewState().isPresent()) {
            Timber.d("View state is already loaded!");
            onViewStateAlreadyLoaded(event);
        } else {
            loadViewState(event);
        }
    }

    protected void onViewStateAlreadyLoaded(Event event) {
    }

    protected Optional<VS> getViewState() {
        return Optional.ofNullable(viewState.getValue());
    }

    protected void loadViewState(Event event) {
    }

    protected Optional<ViewEffect<VS, V>> getViewEffectData() {
        return Optional.ofNullable(viewEffect.getValue());
    }

    protected void updateViewStateAndSet(Consumer<VS> viewStateConsumer) {
        getViewState().ifPresent(vs -> {
            viewStateConsumer.accept(vs);
            viewState.setValue(vs);
        });
    }

    protected void updateViewStateFromBackground(Consumer<VS> viewStateConsumer) {
        uiTask(() -> {
            updateViewStateAndSet(viewStateConsumer);
            return null;
        });
    }

    @NonNull
    protected VS requireViewState() {
        return requireNonNull(viewState.getValue());
    }
}
