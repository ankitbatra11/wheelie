package com.abatra.android.wheelie.architecture;

public interface ViewEffect<VS, V extends IView<VS>> {
    void update(V view);
}
