package com.example.aleksandarmarkovic.yahoonewsfeed.components;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.example.aleksandarmarkovic.yahoonewsfeed.BuildConfig;
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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by aleksandar.markovic on 6/4/2015.
 */

public class SyncService extends IntentService {

    private final static String TAG = SyncService.class.getSimpleName();

    private DatabaseManager databaseManager;
    private ImageDownloader imageDownloader;

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
        DatabaseManager.initializeInstance(new YahooNewsFeedDbHelper(getApplicationContext()));
        databaseManager = DatabaseManager.getInstance();
        imageDownloader = new ImageDownloader(getApplicationContext());
        syncData();
    }

    /**
     * Downloads the data from the server, parse the data
     * and writes it to the database
     */
    public void syncData() {
        if (Utils.isConnected(getApplicationContext())) {
            Log.d(TAG, "Fetching new data from the server");
            String JSONData = fetchDataFromTheServer(Constants.YAHOO_NEWS_REST_RSS_LINK);
            if (JSONData != null) {
                Log.d(TAG, "Parse data from the server");
                parseJSONData(JSONData);
            } else {
                Log.d(TAG, "There is no data returned from the server");
            }
        }
    }

    /**
     * Gets the InputStream from the Given URL if possible
     *
     * @param urlString - url from which we want to get the data
     * @return InputStream from the given url
     */
    private String fetchDataFromTheServer(String urlString) {
        InputStream inputStream = null;
        String dataFromServer = null;
        try {
            URL url = new URL(urlString);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setConnectTimeout(5000);
            httpURLConnection.setReadTimeout(5000);
            httpURLConnection.setInstanceFollowRedirects(true);
            inputStream = httpURLConnection.getInputStream();
            dataFromServer = convertInputStreamToString(inputStream);
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
        return dataFromServer;
    }

    /**
     * Reads data from the given InputStream
     *
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

    /**
     * This function takes a JSON data from the server in the form of the String,
     * and then parse all the relevant records from that data.
     *
     * @param jsonString - Data about news from the server
     */
    private void parseJSONData(String jsonString) {
        JSONObject jsonObject;
        try {
            Log.d(TAG, jsonString + "");
            jsonObject = new JSONObject(jsonString);
            JSONObject results = jsonObject.optJSONObject("query").optJSONObject("results");
            JSONArray resultItems = results.optJSONArray("item");

            final List<SingleNewsItem> newsItemArrayList = new ArrayList<>();

            for (int i = 0; i < resultItems.length(); i++) {
                SingleNewsItem singleNewsItem = parseSingleJSONNewsRecord(resultItems.getJSONObject(i));
                if (singleNewsItem != null) {
                    // we need to check and see if we all ready have this news in the database
                    boolean weHaveThisNews = checkDoWeHaveThisNewsAllReadyInTheDatabase(singleNewsItem);
                    if (!weHaveThisNews) {
                        // add it to the list
                        newsItemArrayList.add(singleNewsItem);
                        // start the download process if this single news item has the image
                        if (singleNewsItem.hasPicture()) {
                            imageDownloader.queuePhoto(singleNewsItem.getUrl(), new ImageDownloader.OnImageDownloadedListener() {
                                @Override
                                public void imageDownloaded(String url, String uri) {
                                    updateImageURIInTheSingleNewsList(newsItemArrayList, url, uri);
                                }
                            });
                        } else {
                            if (BuildConfig.DEBUG) {
                                Log.d(TAG, "This news does not have a picture: " + singleNewsItem.getUrl());
                            }
                        }
                    } else {
                        if (BuildConfig.DEBUG) {
                            Log.d(TAG, "We all ready have this news in the database: " + singleNewsItem.getUrl());
                        }
                    }
                }
            }

            // when all the images are downloaded
            imageDownloader.waitForDownloadToFinish();

            if (BuildConfig.DEBUG) {
                Log.d(TAG, "Write data to the database: " + newsItemArrayList.size());
            }
            // we can then write all the data in the database
            for (SingleNewsItem singleNewsItem : newsItemArrayList) {
                writeRecordToTheDatabase(singleNewsItem);
            }

            if (BuildConfig.DEBUG) {
                Log.d(TAG, "Done writing data to the database");
            }

            // send broadcast that the sync is complete
            Intent i = new Intent();
            i.setAction(Constants.SYNC_COMPLETE_ACTION);
            sendBroadcast(i);

        } catch (JSONException e) {
            Log.e(TAG, "Can't do the proper parsing of the input string" + e.getLocalizedMessage());
        }
    }

    /**
     * Method to process single news.
     * This method will parse and get single news from the JSON object
     *
     * @param jsonObject
     */
    public SingleNewsItem parseSingleJSONNewsRecord(JSONObject jsonObject) {
        SingleNewsItem singleNewsItem = new SingleNewsItem();
        try {
            String title = jsonObject.optString("title");
            singleNewsItem.setTitle(title);
            String description = jsonObject.optString("description");
            singleNewsItem.setDescription(description);
            String url = jsonObject.optString("link");
            singleNewsItem.setUrl(url);
            String pubDate = jsonObject.optString("pubDate");
            singleNewsItem.setPublicationDateAsString(pubDate);

            JSONObject content = jsonObject.optJSONObject("content");
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
            singleNewsItem = null;
        }
        return singleNewsItem;
    }

    /**
     * Checks do we all ready have this news in the database
     *
     * @param singleNewsItem - news that we want to check for
     * @return true if we have news in the database, false otherwise
     */
    private boolean checkDoWeHaveThisNewsAllReadyInTheDatabase(SingleNewsItem singleNewsItem) {

        SQLiteDatabase sqLiteDatabase = null;
        boolean weHaveTheNewsInTheDatabase = false;

        if (singleNewsItem == null || singleNewsItem.getUrl() == null || singleNewsItem.getPublicationDateAsString() == null)
            return weHaveTheNewsInTheDatabase;

        try {
            sqLiteDatabase = databaseManager.openDatabase();

            // where clause only check to see if the url of the news and publication date are the fields
            // that we allready have in the database
            String whereClause = YahooNewsFeedContract.NewsEntry.COLUMN_NAME_URL + " = ? AND " +
                    YahooNewsFeedContract.NewsEntry.COLUMN_NAME_PUB_DAT + " = ?";

            Cursor cursor = sqLiteDatabase.query(YahooNewsFeedContract.NewsEntry.TABLE_NAME,
                    new String[]{YahooNewsFeedContract.NewsEntry.COLUMN_NAME_URL, YahooNewsFeedContract.NewsEntry.COLUMN_NAME_PUB_DAT},
                    whereClause,
                    new String[]{singleNewsItem.getUrl(), singleNewsItem.getPublicationDateAsString()},
                    null,
                    null,
                    null);
            if (cursor != null) {
                weHaveTheNewsInTheDatabase = cursor.getCount() != 0;
            }
        } catch (SQLiteException e) {
            Log.d(TAG, "Database problem: " + e.getLocalizedMessage());
        } finally {
            try {
                if (sqLiteDatabase != null) {
                    databaseManager.closeDatabase();
                }
            } catch (SQLiteException e) {
                Log.d(TAG, "Database problem: " + e.getLocalizedMessage());
            }
        }
        return weHaveTheNewsInTheDatabase;
    }

    /**
     * Appends the image SD card URI to the news with newsURL in the list
     *
     * @param newsItemList - List of all the news
     * @param newsURL      - URL of the news in the list that we wish to add image URI
     * @param imageURI     - URI of the image file on the SD card
     */
    private void updateImageURIInTheSingleNewsList(List<SingleNewsItem> newsItemList, String newsURL, String imageURI) {
        for (SingleNewsItem singleNewsItem : newsItemList) {
            if (singleNewsItem.getUrl().equals(newsURL)) {
                singleNewsItem.setImageSDCardURI(imageURI);
                if (BuildConfig.DEBUG) {
                    Log.d(TAG, "We have updated the SD card URI in the list for " + newsURL);
                }
                return;
            }
        }
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "We did not found news in the list: " + newsURL);
        }
    }

    /**
     * Writes the single news record into the database, we put constraint about unique filds in the
     * database, so if we try to insert News that has the same URL or Publication Data, we will
     * get exception, so only single news data will get written.
     *
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
        return rowID;
    }
}
