package com.example.aleksandarmarkovic.yahoonewsfeed.components;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.example.aleksandarmarkovic.yahoonewsfeed.database.SingleNewsItem;
import com.example.aleksandarmarkovic.yahoonewsfeed.utils.Constants;
import com.example.aleksandarmarkovic.yahoonewsfeed.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by aleksandar.markovic on 6/4/2015.
 */
public class SyncService extends IntentService {

    private final static String TAG = SyncService.class.getSimpleName();


    public SyncService() {
        super("SyncService");
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public SyncService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "Sync started");
        syncData();
    }

    /**
     * Downloads the data from the server,
     * and writes it to the database
     */
    public void syncData() {
        if (Utils.isConnected(getApplicationContext())) {
            String JSONData = downloadDataFromTheRssURL();
            Log.d(TAG, JSONData);
            List<SingleNewsItem> singleNewsList = processJSONData(JSONData);
            //saveDataInTheDatabase(singleNewsList);
        }
    }

    private List<SingleNewsItem> processJSONData(String jsonString) {
        ArrayList<SingleNewsItem> singleNewsItemArrayList = new ArrayList<>();
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(jsonString);
            JSONObject results = jsonObject.optJSONObject("query").optJSONObject("results");
            JSONArray resultItems = results.optJSONArray("item");
            for (int i = 0; i < resultItems.length(); i++) {
                JSONObject item = resultItems.getJSONObject(i);
                String title = item.optString("title");
                String description = item.optString("description");
                String line = item.optString("link");
                String pubDate = item.optString("pubDate");

                JSONObject content = item.optJSONObject("content");
                if (content != null) {

                }

                Log.d(TAG, title);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return singleNewsItemArrayList;
    }

    /**
     * Downloads data from the internet
     * @return String that represents the data
     */
    private String downloadDataFromTheRssURL() {
        String result = "";
        InputStream inputStream = null;
        try {
            URL url = new URL(Constants.YAHOO_NEWS_REST_RSS_LINK);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            inputStream = httpURLConnection.getInputStream();
            result = convertInputStreamToString(inputStream);
        } catch (MalformedURLException exception) {
            Log.d(TAG, "Malformed URL: " + Constants.YAHOO_NEWS_REST_RSS_LINK);
            result = "Malformed URL: " + Constants.YAHOO_NEWS_REST_RSS_LINK;
        } catch (IOException e) {
            Log.d(TAG, "IOException: " + e.getLocalizedMessage());
            result = "IOException: " + e.getLocalizedMessage();
        } finally {
            try {
                if (inputStream != null)
                    inputStream.close();
            } catch (IOException e) {
                Log.d(TAG, e.getLocalizedMessage());
                result = "IOException: Error closing input stream";
            }
        }
        return result;
    }

    /**
     * Reades data from the given InputStream
     * @param inputStream - Stream from which to read the data
     * @return String representing the data
     */
    private String convertInputStreamToString(InputStream inputStream) {
        BufferedReader reader = null;
        String result = "";
        try {
            reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            result = stringBuilder.toString();
        } catch (IOException e) {
            Log.d(TAG, "IOException: " + e.getLocalizedMessage());
            result = "IOException: Error opening BufferReader";
        } finally {
            try {
                if (reader != null)
                    reader.close();
            } catch (Exception e) {
                Log.d(TAG, "IOException: " + e.getLocalizedMessage());
                result = "IOException: Error closing BufferedReader";
            }
        }
        return result;
    }

}
