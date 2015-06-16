package com.example.aleksandarmarkovic.yahoonewsfeed;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.example.aleksandarmarkovic.yahoonewsfeed.components.SyncService;
import com.example.aleksandarmarkovic.yahoonewsfeed.database.SingleNewsItem;
import com.example.aleksandarmarkovic.yahoonewsfeed.utils.Utils;

import org.parceler.Parcels;


/**
 * An activity representing a list of NewsFeeds. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link NewsFeedDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 * <p/>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link NewsFeedListFragment} and the item details
 * (if present) is a {@link NewsFeedDetailFragment}.
 * <p/>
 * This activity also implements the required
 * {@link NewsFeedListFragment.Callbacks} interface
 * to listen for item selections.
 */
public class NewsFeedListActivity extends FragmentActivity
        implements NewsFeedListFragment.Callbacks {

    private static final String TAG = NewsFeedListActivity.class.getSimpleName();

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newsfeed_list);

        if (savedInstanceState == null) {
            doTheStartupActivitySync();
            Utils.setSync(this);
        }

        if (findViewById(R.id.newsfeed_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-large and
            // res/values-sw600dp). If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }
    }

    private void doTheStartupActivitySync() {
        Intent intent = new Intent(getApplicationContext(), SyncService.class);
        startService(intent);
    }

    /**
     * Callback method from {@link NewsFeedListFragment.Callbacks}
     * indicating that the item with the given ID was selected.
     */
    @Override
    public void onItemSelected(SingleNewsItem singleNewsItem) {
        Log.d(TAG, singleNewsItem.getTitle());
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putParcelable(NewsFeedDetailFragment.SINGLE_NEWS_ITEM_PARCELABLE, Parcels.wrap(singleNewsItem));
            NewsFeedDetailFragment fragment = new NewsFeedDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.newsfeed_detail_container, fragment)
                    .commit();

        } else {
            // In single-pane mode, simply start the detail activity
            // for the selected item ID.
            Intent detailIntent = new Intent(this, NewsFeedDetailActivity.class);
            detailIntent.putExtra(NewsFeedDetailFragment.SINGLE_NEWS_ITEM_PARCELABLE, Parcels.wrap(singleNewsItem));
            startActivity(detailIntent);
        }
    }
}
