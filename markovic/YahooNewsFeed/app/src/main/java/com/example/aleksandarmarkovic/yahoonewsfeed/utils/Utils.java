package com.example.aleksandarmarkovic.yahoonewsfeed.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.StrictMode;
import android.os.SystemClock;
import android.widget.Toast;

import com.example.aleksandarmarkovic.yahoonewsfeed.NewsFeedDetailActivity;
import com.example.aleksandarmarkovic.yahoonewsfeed.NewsFeedListActivity;
import com.example.aleksandarmarkovic.yahoonewsfeed.components.BootReceiver;
import com.example.aleksandarmarkovic.yahoonewsfeed.components.SyncService;

/**
 * Created by aleksandar.markovic on 6/5/2015.
 */
public class Utils {

    private Utils() {
    }

    ;

    /**
     * Starts the Sync Alarm
     *
     * @param context
     */
    public static void setTheSyncAlarm(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, SyncService.class);
        PendingIntent pendingIntent =
                PendingIntent.getService(context, 0, intent, 0);
        alarmManager.setInexactRepeating(
                AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + AlarmManager.INTERVAL_FIFTEEN_MINUTES,
                AlarmManager.INTERVAL_FIFTEEN_MINUTES,
                pendingIntent
        );
        //TODO remove this toast
        Toast.makeText(context, "We started the alarm", Toast.LENGTH_SHORT).show();
    }

    /**
     * Set the Sync process,
     * and enables the boot receiver, that will restart the alarm, in case of the device reboot
     *
     * @param context
     */
    public static void setSync(Context context) {
        if (!isSyncOn(context)) {
            setTheSyncAlarm(context);
            enableTheBootLoaderReceiver(context);
            saveSyncToSharedPreferance(context, true);
        }
    }

    /**
     * Enables the BootLoader receiver, so once the device is rebooted the alarm turned on again.
     *
     * @param context
     */
    private static void enableTheBootLoaderReceiver(Context context) {
        ComponentName receiver = new ComponentName(context, BootReceiver.class);
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }

    /**
     * Check to see if the syncing with the server is all ready on
     * We are using Shared Preferences to save the boolean flag
     *
     * @param context
     * @return true or false, depending on the state of the sync
     */
    public static boolean isSyncOn(Context context) {
        SharedPreferences sharedPreferences =
                context.getSharedPreferences(Constants.PREFERENCE_NAME, 0);
        boolean syncIsOn = sharedPreferences.getBoolean(Constants.PREFERENCE_SYNC_SETTING, false);
        return syncIsOn;
    }

    /**
     * Saves the status of the sync to the given valueToSave
     *
     * @param context
     * @param valueToSave
     */
    public static void saveSyncToSharedPreferance(Context context, boolean valueToSave) {
        SharedPreferences sharedPreferences =
                context.getSharedPreferences(Constants.PREFERENCE_NAME, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(Constants.PREFERENCE_SYNC_SETTING, valueToSave);
        editor.commit();
    }

    /**
     * Check to see if we have internet connection turned on
     *
     * @param context
     * @return true/false depending on the internet connection
     */
    public static boolean isConnected(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static void enableStrictMode() {
        if (Utils.hasGingerbread()) {
            StrictMode.ThreadPolicy.Builder threadPolicyBuilder =
                    new StrictMode.ThreadPolicy.Builder()
                            .detectAll()
                            .penaltyLog();
            StrictMode.VmPolicy.Builder vmPolicyBuilder =
                    new StrictMode.VmPolicy.Builder()
                            .detectAll()
                            .penaltyLog();

            if (Utils.hasHoneycomb()) {
                threadPolicyBuilder.penaltyFlashScreen();
                vmPolicyBuilder
                        .setClassInstanceLimit(NewsFeedListActivity.class, 1)
                        .setClassInstanceLimit(NewsFeedDetailActivity.class, 1);
            }
            StrictMode.setThreadPolicy(threadPolicyBuilder.build());
            StrictMode.setVmPolicy(vmPolicyBuilder.build());
        }
    }

    public static boolean hasFroyo() {
        // Can use static final constants like FROYO, declared in later versions
        // of the OS since they are inlined at compile time. This is guaranteed behavior.
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
    }

    public static boolean hasGingerbread() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
    }

    public static boolean hasHoneycomb() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }

    public static boolean hasHoneycombMR1() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1;
    }

    public static boolean hasJellyBean() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
    }

    public static boolean hasKitKat() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }
}
