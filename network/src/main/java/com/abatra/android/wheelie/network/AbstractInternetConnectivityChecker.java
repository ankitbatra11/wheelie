package com.abatra.android.wheelie.network;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

abstract public class AbstractInternetConnectivityChecker implements InternetConnectivityChecker {

    protected final MutableLiveData<Boolean> isConnectedLiveData = new MutableLiveData<>();

    @Override
    public void onResume() {
        startListening();
        updateIsConnectedLiveData();
    }

    protected abstract void startListening();

    protected abstract void stopListening();

    @Override
    public LiveData<Boolean> isConnectedToInternet() {
        updateIsConnectedLiveData();
        return Transformations.distinctUntilChanged(isConnectedLiveData);
    }

    protected abstract void updateIsConnectedLiveData();
}
