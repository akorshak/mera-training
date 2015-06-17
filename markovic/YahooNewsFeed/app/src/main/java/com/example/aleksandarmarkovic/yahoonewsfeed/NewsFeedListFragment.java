package com.example.aleksandarmarkovic.yahoonewsfeed;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.aleksandarmarkovic.yahoonewsfeed.components.NewsLoader;
import com.example.aleksandarmarkovic.yahoonewsfeed.database.SingleNewsItem;

import java.util.List;

/**
 * A list fragment representing a list of NewsFeeds.
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class NewsFeedListFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<SingleNewsItem>> {

    public static final String TAG = NewsFeedListFragment.class.getSimpleName();

    /**
     * The Loader's id (this id is specific to the ListFragment's LoaderManager)
     */
    private static final int LOADER_ID = 1;

    /**
     * A dummy implementation of the {@link Callbacks} interface that does
     * nothing. Used only when this fragment is not attached to an activity.
     */
    private static Callbacks sDummyCallbacks = new Callbacks() {
        @Override
        public void onItemSelected(SingleNewsItem id) {
        }
    };
    private NewsListAdapter newsListAdapter;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    /**
     * The fragment's current callback object, which is notified of list item
     * clicks.
     */
    private Callbacks mCallbacks = sDummyCallbacks;

    /**
     * Query text that the user enteres in the Search View in the NewsFeedListActivity toolbar,
     * and which is provided to this fragment, so we can restart loader, and load new data
     * from the database.
     */
    private String queryText;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public NewsFeedListFragment() {
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        newsListAdapter = new NewsListAdapter(new NewsListAdapter.NewsItemOnClickListener() {
            @Override
            public void onNewsClicked(SingleNewsItem singleNewsItem) {
                mCallbacks.onItemSelected(singleNewsItem);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(newsListAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        progressBar.setVisibility(View.VISIBLE);

        if (BuildConfig.DEBUG) {
            Log.i(TAG, "+++ Calling initLoader()! +++");
            if (getLoaderManager().getLoader(LOADER_ID) == null) {
                Log.i(TAG, "+++ Initializing the new Loader... +++");
            } else {
                Log.i(TAG, "+++ Reconnecting with existing Loader (id '1')... +++");
            }
        }

        getLoaderManager().initLoader(LOADER_ID, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_newsfeed_list, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        progressBar = (ProgressBar) view.findViewById(R.id.loading_progress_bar);
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // Activities containing this fragment must implement its callbacks.
        if (!(activity instanceof Callbacks)) {
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }

        mCallbacks = (Callbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        // Reset the active callbacks interface to the dummy implementation.
        mCallbacks = sDummyCallbacks;
    }

    @Override
    public Loader<List<SingleNewsItem>> onCreateLoader(int id, Bundle args) {
        if (BuildConfig.DEBUG)
            Log.i(TAG, "+++ onCreateLoader() called! +++");
        return new NewsLoader(getActivity(), queryText);
    }

    @Override
    public void onLoadFinished(Loader<List<SingleNewsItem>> loader, List<SingleNewsItem> data) {
        if (BuildConfig.DEBUG)
            Log.i(TAG, "+++ onLoadFinished() called! +++");
        newsListAdapter.setData(data);
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onLoaderReset(Loader<List<SingleNewsItem>> loader) {
        if (BuildConfig.DEBUG)
            Log.i(TAG, "+++ onLoadReset() called! +++");
        newsListAdapter.setData(null);
    }

    public void setNewSearchQuery(String queryText) {
        this.queryText = queryText;
        getLoaderManager().restartLoader(LOADER_ID, null, this);
    }

    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface Callbacks {
        /**
         * Callback for when an item has been selected.
         */
        public void onItemSelected(SingleNewsItem singleNewsItem);
    }
}
