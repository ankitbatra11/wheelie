package com.abatra.android.wheelie.core.network;

import com.abatra.android.wheelie.core.designpattern.Observable;

public interface InternetConnectivityChecker extends Observable<InternetConnectivityListener> {
    /**
     * Starts checking internet connection.
     */
    void startChecking();

    /**
     * @return True if connected to internet, otherwise False.
     */
    boolean isConnectedToInternet();

    /**
     * Stop checking internet connection.
     */
    void stopChecking();

}
