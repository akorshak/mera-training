package com.example.aleksandarmarkovic.yahoonewsfeed.receivers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import com.example.aleksandarmarkovic.yahoonewsfeed.utils.Utils;

/**
 * Created by aleksandar.markovic on 6/4/2015.
 */
public class BootReceiver extends BroadcastReceiver {

    private final static String TAG = BootReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            Utils.setTheSyncAlarm(context);
        }
    }
}
