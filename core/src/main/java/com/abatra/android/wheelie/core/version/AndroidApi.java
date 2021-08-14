package com.abatra.android.wheelie.core.version;

import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.abatra.android.wheelie.core.network.InternetConnectivityChecker;
import com.abatra.android.wheelie.core.network.PreQInternetConnectivityChecker;
import com.abatra.android.wheelie.core.network.QOrAboveInternetConnectivityChecker;

public class AndroidApi {

    private static final AndroidApi ANDROID_API = new AndroidApi();

    public static AndroidApi getInstance() {
        return ANDROID_API;
    }

    public void startServiceFromBackground(Context context, Intent serviceIntent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(serviceIntent);
        } else {
            context.startService(serviceIntent);
        }
    }

    public InternetConnectivityChecker internetConnectivityChecker(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return QOrAboveInternetConnectivityChecker.newInstance(context);
        } else {
            return PreQInternetConnectivityChecker.newInstance(context);
        }
    }
}
