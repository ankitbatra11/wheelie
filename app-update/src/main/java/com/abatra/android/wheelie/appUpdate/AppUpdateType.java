package com.abatra.android.wheelie.appUpdate;

public enum AppUpdateType {
    FLEXIBLE {
        @Override
        public int getPlayStoreAppUpdateType() {
            return com.google.android.play.core.install.model.AppUpdateType.FLEXIBLE;
        }
    },
    IMMEDIATE {
        @Override
        public int getPlayStoreAppUpdateType() {
            return com.google.android.play.core.install.model.AppUpdateType.IMMEDIATE;
        }
    };

    public abstract int getPlayStoreAppUpdateType();
}
