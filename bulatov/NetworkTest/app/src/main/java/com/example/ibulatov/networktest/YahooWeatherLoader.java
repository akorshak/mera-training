package com.example.ibulatov.networktest;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

public class YahooWeatherLoader extends AsyncTaskLoader<YahooWeatherItem> {

    private static final String LOG_TAG = "MainActivity";
    private YahooWeatherItem mData;

    public YahooWeatherLoader(Context context) {
        super(context);
        Log.d(LOG_TAG, "constructor");
    }

    @Override
    public void deliverResult(YahooWeatherItem data) {
        Log.d(LOG_TAG, " deliverResult");

        if (isReset()) {
            return;
        }

        mData = data;

        if (isStarted()) {
            super.deliverResult(data);
        }
    }

    @Override
    protected void onStartLoading() {
        Log.d(LOG_TAG, " onStartLoading");

        if (mData != null) {
            deliverResult(mData);
        } else {
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading() {
        Log.d(LOG_TAG, " onStopLoading");
        cancelLoad();
    }

    @Override
    protected void onReset() {
        Log.d(LOG_TAG, " onReset");
        onStopLoading();

        if (mData != null) {
            mData = null;
        }
    }

    @Override
    protected void onForceLoad() {
        super.onForceLoad();
        Log.d(LOG_TAG, "onForceLoad");
    }

    @Override
    protected void onAbandon() {
        super.onAbandon();
        Log.d(LOG_TAG, " onAbandon");
    }

    @Override
    public YahooWeatherItem loadInBackground() {

        YahooWeatherClient fetcher = new YahooWeatherClient();
        YahooWeatherItem item = fetcher.fetchForecast();

        return item;
    }
}
