package com.example.aleksandarmarkovic.yahoonewsfeed.components;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.example.aleksandarmarkovic.yahoonewsfeed.database.DatabaseManager;
import com.example.aleksandarmarkovic.yahoonewsfeed.database.SingleNewsItem;
import com.example.aleksandarmarkovic.yahoonewsfeed.database.YahooNewsFeedContract;
import com.example.aleksandarmarkovic.yahoonewsfeed.database.YahooNewsFeedDbHelper;
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

/**
 * Created by aleksandar.markovic on 6/4/2015.
 */
public class SyncService extends IntentService {

    private final static String TAG = SyncService.class.getSimpleName();

    DatabaseManager databaseManager;

    public SyncService() {
        super("SyncService");
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     * @param name Used to name the worker thread, important only for debugging.
     */
    public SyncService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "Sync started");
        DatabaseManager.initializeInstance(new YahooNewsFeedDbHelper(getApplicationContext()));
        databaseManager = DatabaseManager.getInstance();
        syncData();
    }

    /**
     * Downloads the data from the server,
     * and writes it to the database
     */
    public void syncData() {
        if (Utils.isConnected(getApplicationContext())) {
            InputStream inputStream = getInputStreamFromURL(Constants.YAHOO_NEWS_REST_RSS_LINK);
            String JSONData = convertInputStreamToString(inputStream);
            processJSONData(JSONData);
        }
    }

    /**
     * This function takes a JSON data from the server in the form of the String,
     * and then parse all the relevant records from that data.
     * @param jsonString - Data about news from the server
     */
    private void processJSONData(String jsonString) {
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(jsonString);
            JSONObject results = jsonObject.optJSONObject("query").optJSONObject("results");
            JSONArray resultItems = results.optJSONArray("item");
            for (int i = 0; i < resultItems.length(); i++) {
                parseSingleJSONNewsRecord(resultItems.getJSONObject(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to process single news.
     * This method will parse and get all the news data
     * and then:
     *  1) Write data to the database
     *  2) Store the image on the SD card as a cache reference
     * @param item
     */
    public void parseSingleJSONNewsRecord(JSONObject item) {
        SingleNewsItem singleNewsItem = new SingleNewsItem();
        try {
            String title = item.optString("title");
            singleNewsItem.setTitle(title);
            String description = item.optString("description");
            singleNewsItem.setDescription(description);
            String url = item.optString("link");
            singleNewsItem.setUrl(url);
            String pubDate = item.optString("pubDate");
            singleNewsItem.setPublicationDateAsString(pubDate);

            JSONObject content = item.optJSONObject("content");
            if (content != null) {
                String contentType = content.getString("type");
                if (contentType.startsWith("image/")) {
                    int width = content.getInt("width");
                    int height = content.getInt("height");
                    String imageURL = content.getString("url");
                    singleNewsItem.setImage(contentType, imageURL, width, height);
                }
            }
        } catch (JSONException jsonException) {
            Log.d(TAG, "There was an error in processing this single news");
            jsonException.printStackTrace();
        }

        saveSingleNewsRecord(singleNewsItem);
    }

    /**
     * This method saves the single News record in the database
     * 1) saves the data about the news in the database
     * 2) if the news has a picture, saves the picture on the SD card
     * 3) updates the record in the database to include the URI from the SD card location
     *
     * @param singleNewsItem
     */
    private void saveSingleNewsRecord(SingleNewsItem singleNewsItem) {
        long rowID = writeRecordToTheDatabase(singleNewsItem);

    }

    /**
     * Writes the single news record into the database, we put constraint about unique filds in the
     * database, so if we try to insert News that has the same URL or Publication Data, we will
     * get exception, so only single news data will get writen.
     * @param singleNewsItem - Single news item that needs to be written in the database
     * @return - row of that
     */
    private long writeRecordToTheDatabase(SingleNewsItem singleNewsItem) {
        ContentValues contentValues = singleNewsItem.createContentValues();

        long rowID = -1;
        SQLiteDatabase sqLiteDatabase = null;
        try {
            sqLiteDatabase = databaseManager.openDatabase();
            rowID = sqLiteDatabase.insertOrThrow(YahooNewsFeedContract.NewsEntry.TABLE_NAME,
                    null,
                    contentValues
            );
        } catch (SQLiteConstraintException exception) {
            Log.d(TAG, "Constraint rule error: " + exception.getLocalizedMessage());
        } catch (SQLiteException exception) {
            Log.d(TAG, "openDatabase error: " + exception.getLocalizedMessage());
        } catch (SQLException exception) {
            Log.d(TAG, "Something terrible wrong: " + exception.getLocalizedMessage());
        } finally {
            try {
                if (sqLiteDatabase != null && databaseManager != null)
                    databaseManager.closeDatabase();
            } catch (SQLiteException exception) {
                Log.d(TAG, "closeDatabase error: " + exception.getLocalizedMessage());
            }
        }

        if (rowID == -1) {
            Log.d(TAG, "Something went wrong, or no data inserted");
        } else {
            Log.d(TAG, "We have the new data in the database, rowID: " + rowID);
        }
    }

    /**
     * Gets the InputStream from the Given URL if possible
     * @param urlString - url from which we want to get the data
     * @return InputStream from the given url
     */
    private InputStream getInputStreamFromURL(String urlString) {
        InputStream inputStream = null;
        try {
            URL url = new URL(urlString);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            inputStream = httpURLConnection.getInputStream();
        } catch (MalformedURLException exception) {
            Log.d(TAG, "Malformed URL: " + Constants.YAHOO_NEWS_REST_RSS_LINK);
        } catch (IOException e) {
            Log.d(TAG, "IOException: " + e.getLocalizedMessage());
        } finally {
            try {
                if (inputStream != null)
                    inputStream.close();
            } catch (IOException e) {
                Log.d(TAG, e.getLocalizedMessage());
            }
        }
        return inputStream;
    }

    /**
     * Reads data from the given InputStream
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
