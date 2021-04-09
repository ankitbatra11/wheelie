package com.abatra.android.wheelie.activity;

import androidx.appcompat.app.AppCompatActivity;

import com.abatra.android.wheelie.lifecycle.ILifecycleOwner;

public class WheelieActivity extends AppCompatActivity implements ILifecycleOwner {

    protected ActivityStarter getActivityStarter() {
        return ActivityStarter.of(this);
    }
}
