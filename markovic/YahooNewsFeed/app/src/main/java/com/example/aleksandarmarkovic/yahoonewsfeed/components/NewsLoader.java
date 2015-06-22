package com.example.aleksandarmarkovic.yahoonewsfeed.components;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.example.aleksandarmarkovic.yahoonewsfeed.BuildConfig;
import com.example.aleksandarmarkovic.yahoonewsfeed.database.DatabaseManager;
import com.example.aleksandarmarkovic.yahoonewsfeed.database.SingleNewsItem;
import com.example.aleksandarmarkovic.yahoonewsfeed.database.YahooNewsFeedContract;
import com.example.aleksandarmarkovic.yahoonewsfeed.database.YahooNewsFeedDbHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aleksandar.markovic on 6/12/2015.
 */
public class NewsLoader extends AsyncTaskLoader<List<SingleNewsItem>> {

    private static final String TAG = NewsLoader.class.getSimpleName();

    // We hold a reference to the Loader's data here.
    private List<SingleNewsItem> singleNewsItems;
    /**
     * *****************************************************************
     */

    // An observer to notify the Loader when we have new news in the database.
    private SyncCompleteReceiver newNewsObserver;

    /**
     * That on which we base our search in the database, Every news item that has this text in
     * its Title will be put in the search result list that this Loader provides
     */
    private String searchQueryText;

    /****************************************************/
    /** (1) A task that performs the asynchronous load **/
    /**
     * ************************************************
     */

    public NewsLoader(Context ctx, String queryText) {
        // Loaders may be used across multiple Activitys (assuming they aren't
        // bound to the LoaderManager), so NEVER hold a reference to the context
        // directly. Doing so will cause you to leak an entire Activity's context.
        // The superclass constructor will store a reference to the Application
        // Context instead, and can be retrieved with a call to getContext().
        super(ctx);
        if (queryText == null) {
            this.searchQueryText = "";
        } else {
            this.searchQueryText = queryText;
        }
    }

    /*******************************************/
    /** (2) Deliver the results to the client **/
    /*******************************************/

    /**
     * This method is called on a background thread and generates a List of
     * {@link SingleNewsItem} objects. Each entry corresponds to a single News in the database.
     */
    @Override
    public List<SingleNewsItem> loadInBackground() {
        if (BuildConfig.DEBUG)
            Log.i(TAG, "+++ loadInBackground() called! +++");

        //TODO Retrieve all news from the database
        List<SingleNewsItem> newsItems = null;

        if (newsItems == null) {
            newsItems = new ArrayList<>();
        }

        //TODO add limit clausule, consider adding some index to title column
        String WHERE = YahooNewsFeedContract.NewsEntry.COLUMN_NAME_TITLE + " LIKE ? ";

        DatabaseManager.initializeInstance(new YahooNewsFeedDbHelper(getContext()));
        DatabaseManager databaseManager = DatabaseManager.getInstance();
        Cursor cursor = databaseManager.openDatabase().query(YahooNewsFeedContract.NewsEntry.TABLE_NAME,
                YahooNewsFeedContract.NewsEntry.SELECT_ALL,
                WHERE,
                new String[]{"%" + searchQueryText + "%"},
                null,
                null,
                null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    String title = cursor.getString(cursor.getColumnIndex(YahooNewsFeedContract.NewsEntry.COLUMN_NAME_TITLE));
                    String description = cursor.getString(cursor.getColumnIndex(YahooNewsFeedContract.NewsEntry.COLUMN_NAME_DESCRIPTION));
                    String publicationDateAsString = cursor.getString(cursor.getColumnIndex(YahooNewsFeedContract.NewsEntry.COLUMN_NAME_PUB_DAT));
                    String url = cursor.getString(cursor.getColumnIndex(YahooNewsFeedContract.NewsEntry.COLUMN_NAME_URL));
                    SingleNewsItem singleNewsItem = new SingleNewsItem(title, description, publicationDateAsString, url);

                    String imageUrl = cursor.getString(cursor.getColumnIndex(YahooNewsFeedContract.NewsEntry.COLUMN_NAME_IMAGE_URL));
                    if (imageUrl != null && !imageUrl.equals("")) {
                        String imageSDUri = cursor.getString(cursor.getColumnIndex(YahooNewsFeedContract.NewsEntry.COLUMN_NAME_IMAGE_SD_URI));
                        String imageType = cursor.getString(cursor.getColumnIndex(YahooNewsFeedContract.NewsEntry.COLUMN_NAME_IMAGE_TYPE));
                        int width = Integer.parseInt(cursor.getString(cursor.getColumnIndex(YahooNewsFeedContract.NewsEntry.COLUMN_NAME_IMAGE_WIDTH)));
                        int height = Integer.parseInt(cursor.getString(cursor.getColumnIndex(YahooNewsFeedContract.NewsEntry.COLUMN_NAME_IMAGE_HEIGHT)));
                        singleNewsItem.setImage(imageType, imageUrl, width, height);
                        singleNewsItem.setImageSDCardURI(imageSDUri);
                    }
                    newsItems.add(singleNewsItem);
                } while (cursor.moveToNext());
            }
        } else {
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "There is no data in the database");
            }
        }

        databaseManager.closeDatabase();

        if (BuildConfig.DEBUG) {
            Log.d(TAG, "Size of the data from the database: " + newsItems.size());
        }

        return newsItems;
    }

    /*********************************************************/
    /** (3) Implement the Loader?s state-dependent behavior **/

    /**
     * Called when there is new data to deliver to the client. The superclass will
     * deliver it to the registered listener (i.e. the LoaderManager), which will
     * forward the results to the client through a call to onLoadFinished.
     */
    @Override
    public void deliverResult(List<SingleNewsItem> newsItems) {
        if (isReset()) {
            if (BuildConfig.DEBUG)
                Log.w(TAG, "+++ Warning! An async query came in while the Loader was reset! +++");
            // The Loader has been reset; ignore the result and invalidate the data.
            // This can happen when the Loader is reset while an asynchronous query
            // is working in the background. That is, when the background thread
            // finishes its work and attempts to deliver the results to the client,
            // it will see here that the Loader has been reset and discard any
            // resources associated with the new data as necessary.
            if (newsItems != null) {
                releaseResources(newsItems);
                return;
            }
        }

        // Hold a reference to the old data so it doesn't get garbage collected.
        // We must protect it until the new data has been delivered.
        List<SingleNewsItem> newsFromPreviousLoad = singleNewsItems;
        singleNewsItems = newsItems;

        if (isStarted()) {
            if (BuildConfig.DEBUG) Log.i(TAG, "+++ Delivering results to the LoaderManager for" +
                    " the ListFragment to display! +++");
            // If the Loader is in a started state, have the superclass deliver the
            // results to the client.
            super.deliverResult(newsItems);
        }

        // Invalidate the old data as we don't need it any more.
        if (newsFromPreviousLoad != null && newsFromPreviousLoad != newsItems) {
            if (BuildConfig.DEBUG)
                Log.i(TAG, "+++ Releasing any old data associated with this Loader. +++");
            releaseResources(newsFromPreviousLoad);
        }
    }

    /**
     * *****************************************************
     */

    @Override
    protected void onStartLoading() {
        if (BuildConfig.DEBUG) Log.i(TAG, "+++ onStartLoading() called! +++");

        if (singleNewsItems != null) {
            // Deliver any previously loaded data immediately.
            if (BuildConfig.DEBUG)
                Log.i(TAG, "+++ Delivering previously loaded data to the client...");
            deliverResult(singleNewsItems);
        }

        // Register the observers that will notify the Loader when changes are made.
        if (newNewsObserver == null) {
            newNewsObserver = new SyncCompleteReceiver(this);
        }

        if (takeContentChanged()) {
            // When the observer detects a new news from the server, it will call
            // onContentChanged() on the Loader, which will cause the next call to
            // takeContentChanged() to return true. If this is ever the case (or if
            // the current data is null), we force a new load.
            if (BuildConfig.DEBUG)
                Log.i(TAG, "+++ A content change has been detected... so force load! +++");
            forceLoad();
        } else if (singleNewsItems == null) {
            // If the current data is null... then we should make it non-null! :)
            if (BuildConfig.DEBUG)
                Log.i(TAG, "+++ The current data is data is null... so force load! +++");
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading() {
        if (BuildConfig.DEBUG) Log.i(TAG, "+++ onStopLoading() called! +++");

        // The Loader has been put in a stopped state, so we should attempt to
        // cancel the current load (if there is one).
        cancelLoad();

        // Note that we leave the observer as is; Loaders in a stopped state
        // should still monitor the data source for changes so that the Loader
        // will know to force a new load if it is ever started again.
    }

    @Override
    protected void onReset() {
        if (BuildConfig.DEBUG) Log.i(TAG, "+++ onReset() called! +++");

        // Ensure the loader is stopped.
        onStopLoading();

        // At this point we can release the resources associated with 'singleNewsItems'.
        if (singleNewsItems != null) {
            releaseResources(singleNewsItems);
            singleNewsItems = null;
        }

        // The Loader is being reset, so we should stop monitoring for changes.
        if (newNewsObserver != null) {
            getContext().unregisterReceiver(newNewsObserver);
            newNewsObserver = null;
        }
    }

    @Override
    public void onCanceled(List<SingleNewsItem> apps) {
        if (BuildConfig.DEBUG) Log.i(TAG, "+++ onCanceled() called! +++");

        // Attempt to cancel the current asynchronous load.
        super.onCanceled(apps);

        // The load has been canceled, so we should release the resources
        // associated with 'singleNewsItems'.
        releaseResources(apps);
    }

    @Override
    public void forceLoad() {
        if (BuildConfig.DEBUG) Log.i(TAG, "+++ forceLoad() called! +++");
        super.forceLoad();
    }

    /*********************************************************************/
    /** (4) Observer which receives notifications when the data changes **/

    /**
     * Helper method to take care of releasing resources associated with an
     * actively loaded data set.
     */
    private void releaseResources(List<SingleNewsItem> apps) {
        // For a simple List, there is nothing to do. For something like a Cursor,
        // we would close it in this method. All resources associated with the
        // Loader should be released here.
    }

}
