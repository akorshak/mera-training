package com.example.ibulatov.networktest;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

public class NetworkChecker {

    private ConnectivityManager connectivityManager;
    private TelephonyManager telephonyManager;

    public NetworkChecker(Context context) {
        this.connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        this.telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
    }

    public boolean isConnected() {
        return isConnectedToCellNetwork() || isConnectedToWifiNetwork();
    }

    private boolean isConnectedToCellNetwork() {
        int simState = telephonyManager.getSimState();
        if (simState == TelephonyManager.SIM_STATE_READY) {
            NetworkInfo networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            return networkInfo.isConnectedOrConnecting();
        }
        return false;
    }

    private boolean isConnectedToWifiNetwork() {
        NetworkInfo networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return networkInfo.isConnected();
    }

}
