package com.abatra.android.wheelie.update;

public enum AppUpdateType {
    FLEXIBLE {
        @Override
        public int getPlayStoreAppUpdateType() {
            return com.google.android.play.core.install.model.AppUpdateType.FLEXIBLE;
        }

        @Override
        public void beforeStartingAppUpdateFlow(AppUpdateRequestor appUpdateRequestor) {
            appUpdateRequestor.registerInstallStatusListener();
        }
    },
    IMMEDIATE {
        @Override
        public int getPlayStoreAppUpdateType() {
            return com.google.android.play.core.install.model.AppUpdateType.IMMEDIATE;
        }
    };

    public abstract int getPlayStoreAppUpdateType();

    public void beforeStartingAppUpdateFlow(AppUpdateRequestor appUpdateRequestor) {
    }
}
