package com.example.aleksandarmarkovic.yahoonewsfeed.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by aleksandar.markovic on 6/4/2015.
 */
public class StartSyncServiceReceiver extends BroadcastReceiver {

    private final static String TAG = StartSyncServiceReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        //TODO we need to start the service and sync the data with the database
        Log.d(TAG, "We need to start the sync process here");
    }
}
