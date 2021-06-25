package com.abatra.android.wheelie.appUpdate;

import android.app.Activity;

import androidx.annotation.NonNull;

abstract public class AppUpdateRequest {

    private final AppUpdateType requestedAppUpdateType;
    private final Activity activity;
    private final int reqCode;

    protected AppUpdateRequest(AppUpdateType requestedAppUpdateType, Activity activity, int reqCode) {
        this.requestedAppUpdateType = requestedAppUpdateType;
        this.activity = activity;
        this.reqCode = reqCode;
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

    @NonNull
    @Override
    public String toString() {
        return "AppUpdateRequest{" +
                "requestedAppUpdateType=" + requestedAppUpdateType +
                ", activity=" + activity +
                ", reqCode=" + reqCode +
                '}';
    }
}
