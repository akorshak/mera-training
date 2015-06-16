package com.example.aleksandarmarkovic.yahoonewsfeed;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.aleksandarmarkovic.yahoonewsfeed.database.SingleNewsItem;

import org.parceler.Parcels;

/**
 * A fragment representing a single NewsFeed detail screen.
 * This fragment is either contained in a {@link NewsFeedListActivity}
 * in two-pane mode (on tablets) or a {@link NewsFeedDetailActivity}
 * on handsets.
 */
public class NewsFeedDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String SINGLE_NEWS_ITEM_PARCELABLE = "item_id";

    /**
     * The title this fragment is presenting.
     */
    private SingleNewsItem newsTitle;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public NewsFeedDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(SINGLE_NEWS_ITEM_PARCELABLE)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            newsTitle = Parcels.unwrap(getArguments().getParcelable(SINGLE_NEWS_ITEM_PARCELABLE));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_newsfeed_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (newsTitle != null) {
            ((TextView) rootView.findViewById(R.id.newsfeed_detail)).setText(newsTitle.getTitle());
        }

        return rootView;
    }
}
