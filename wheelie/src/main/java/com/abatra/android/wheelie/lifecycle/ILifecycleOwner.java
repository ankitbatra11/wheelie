package com.abatra.android.wheelie.lifecycle;

import android.content.Context;

import androidx.lifecycle.LifecycleOwner;

public interface ILifecycleOwner extends LifecycleOwner {
    Context getContext();
}
