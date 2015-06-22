package com.example.aleksandarmarkovic.yahoonewsfeed;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.example.aleksandarmarkovic.yahoonewsfeed.database.SingleNewsItem;
import com.gc.materialdesign.views.ProgressBarDeterminate;

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
     * Single news item that we want to display in this page
     */
    private SingleNewsItem singleNewsItem;

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
            singleNewsItem = Parcels.unwrap(getArguments().getParcelable(SINGLE_NEWS_ITEM_PARCELABLE));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_newsfeed_detail, container, false);

        if (singleNewsItem != null) {

            WebView webView = ((WebView) rootView.findViewById(R.id.newsfeed_detail));
            final ProgressBarDeterminate progressBar = (ProgressBarDeterminate) rootView.findViewById(R.id.loading_page_progress_bar);

            webView.getSettings().setJavaScriptEnabled(true);

            webView.setWebChromeClient(new WebChromeClient() {
                public void onProgressChanged(WebView view, int progress) {
                    Log.d("TAG", "Progress: " + progress);
                    progressBar.setProgress(progress);
                    if (progress == 100)
                        progressBar.setVisibility(View.GONE);
                }

            });
            webView.setWebViewClient(new WebViewClient() {
                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                    Toast.makeText(getActivity(), "Error downloading news! " + description, Toast.LENGTH_SHORT).show();
                }
            });

            webView.loadUrl(singleNewsItem.getUrl());
        }

        return rootView;
    }
}
