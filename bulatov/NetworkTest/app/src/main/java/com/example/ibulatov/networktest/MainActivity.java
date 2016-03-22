package com.example.ibulatov.networktest;

import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<YahooWeatherItem>, SwipeRefreshLayout.OnRefreshListener {

    private static final String LOG_TAG = "MainActivity";
    private static final int YAHOO_WEATHER_LOADER = 0;

    private SwipeRefreshLayout swipeRefresh;
    private Loader<YahooWeatherItem> weatherLoader;
    private ListView weatherList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swiper_refreash_layout);
        swipeRefresh.setOnRefreshListener(this);

        weatherList = (ListView) findViewById(R.id.weather_list);
        weatherLoader = getSupportLoaderManager().initLoader(YAHOO_WEATHER_LOADER, null, this);

        if(savedInstanceState == null) {
            swipeRefresh.post(new Runnable() {
                @Override
                public void run() {
                    swipeRefresh.setRefreshing(true);
                    weatherLoader.forceLoad();
                }
            });
        }
    }

    @Override
    public Loader<YahooWeatherItem> onCreateLoader(int id, Bundle args) {
        Log.d(LOG_TAG, "onCreateLoader");
        return id == YAHOO_WEATHER_LOADER ? new YahooWeatherLoader(this) : null;
    }

    @Override
    public void onLoadFinished(Loader<YahooWeatherItem> loader, YahooWeatherItem data) {
        Log.d(LOG_TAG, "onLoadFinished");

        if(swipeRefresh.isRefreshing()) {
            swipeRefresh.setRefreshing(false);
        }

        if(data != null) {
            weatherList.setAdapter(new WeatherListAdapter(this, data));
        }
    }

    @Override
    public void onLoaderReset(Loader<YahooWeatherItem> loader) {
        Log.d(LOG_TAG, "onLoaderReset");
    }

    @Override
    public void onRefresh() {
        Log.d(LOG_TAG, "onRefresh");

        //explicit network status check
        //can't rely on NULL in onLoadFinished because onLoadFinished doesn't get called twice with same data

        if(new NetworkChecker(this).isConnected()) {
            Loader<YahooWeatherItem> loader = getSupportLoaderManager().getLoader(YAHOO_WEATHER_LOADER);
            loader.forceLoad();
        } else {
            Toast.makeText(this, "Problem with loading weather data", Toast.LENGTH_SHORT).show();
            if(swipeRefresh.isRefreshing()) {
                swipeRefresh.setRefreshing(false);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(LOG_TAG, "onStart");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "onDestroy");
    }
}
