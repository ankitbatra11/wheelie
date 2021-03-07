package com.abatra.android.wheelie.update;

import android.app.Activity;

import androidx.annotation.Nullable;

import java.util.Optional;

public class AppUpdateRequest {

    private final AppUpdateType requestedAppUpdateType;
    private final Activity activity;
    private final int reqCode;
    @Nullable
    private AppUpdateRequestor.Observer observer;

    public AppUpdateRequest(AppUpdateType requestedAppUpdateType, Activity activity, int reqCode) {
        this.requestedAppUpdateType = requestedAppUpdateType;
        this.activity = activity;
        this.reqCode = reqCode;
    }

    public AppUpdateRequest setObserver(AppUpdateRequestor.Observer observer) {
        this.observer = observer;
        return this;
    }

    public Optional<AppUpdateRequestor.Observer> getObserver() {
        return Optional.ofNullable(observer);
    }

    public AppUpdateType getRequestedAppUpdateType() {
        return requestedAppUpdateType;
    }

    public Activity getActivity() {
        return activity;
    }

    public int getReqCode() {
        return reqCode;
    }

    @Override
    public String toString() {
        return "AppUpdateRequest{" +
                "requestedAppUpdateType=" + requestedAppUpdateType +
                ", activity=" + activity +
                ", reqCode=" + reqCode +
                ", observer=" + observer +
                '}';
    }
}
