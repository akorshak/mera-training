package com.example.aleksandarmarkovic.yahoonewsfeed.components;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.example.aleksandarmarkovic.yahoonewsfeed.BuildConfig;
import com.example.aleksandarmarkovic.yahoonewsfeed.utils.Constants;

/**
 * Created by aleksandar.markovic on 6/15/2015.
 * Used by the {@link NewsLoader}. An observer that listens for
 * sync complete(and notifies the loader when
 * these changes are detected).
 */
public class SyncCompleteReceiver extends BroadcastReceiver {
    private static final String TAG = SyncCompleteReceiver.class.getSimpleName();

    private NewsLoader mLoader;

    public SyncCompleteReceiver(NewsLoader loader) {
        mLoader = loader;

        // Register for events related to sync complete.
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.SYNC_COMPLETE_ACTION);
        mLoader.getContext().registerReceiver(this, filter);
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent != null && intent.getAction().equals(Constants.SYNC_COMPLETE_ACTION)) {
            if (BuildConfig.DEBUG)
                Log.i(TAG, "+++ The observer has detected a new sync!" +
                        " Notifying Loader... +++");

            // Tell the loader about the change.
            mLoader.onContentChanged();
        }
    }
}
