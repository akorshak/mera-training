package com.example.aleksandarmarkovic.yahoonewsfeed;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.aleksandarmarkovic.yahoonewsfeed.database.SingleNewsItem;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aleksandar.markovic on 6/15/2015.
 */
public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.ViewHolder> {

    private static final String TAG = NewsListAdapter.class.getSimpleName();

    private NewsItemOnClickListener newsItemOnClickListener;
    private List<SingleNewsItem> newsItemList;
    private Context context;

    public NewsListAdapter(NewsItemOnClickListener newsItemOnClickListener, Context context) {
        this.newsItemOnClickListener = newsItemOnClickListener;
        this.newsItemList = new ArrayList<>();
        this.context = context;
    }


    public void setData(List<SingleNewsItem> newData) {
        if (newData != null) {
            newsItemList.clear();
            newsItemList.addAll(newData);
            notifyDataSetChanged();
        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public NewsListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_news_list_item, null);

        // create ViewHolder
        ViewHolder viewHolder = new ViewHolder(itemLayoutView, new ViewHolder.ViewHolderClickListener() {
            @Override
            public void onClick(View v, int position) {
                newsItemOnClickListener.onNewsClicked(newsItemList.get(position));
            }
        });
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        // - get data from your itemsData at this position
        // - replace the contents of the view with that itemsData

        viewHolder.txtViewTitle.setText(newsItemList.get(position).getTitle());
        final SingleNewsItem singleNewsItem = newsItemList.get(position);
        if (singleNewsItem.hasPicture()) {
            Picasso.with(context)
                    .load(newsItemList.get(position).getImage().getImageURL())
                            //.transform(new FitToTargetViewTransformation(viewHolder.imgViewIcon))
                    .fit().centerCrop()
                    .into(viewHolder.imgViewIcon, new Callback() {
                        @Override
                        public void onSuccess() {
                            Log.d(TAG, "Image loading success");
                        }

                        @Override
                        public void onError() {
                            Log.d(TAG, "Could not load this image: " + singleNewsItem.getImage().getImageURI());
                        }
                    });
        } else {
            viewHolder.imgViewIcon.setImageResource(R.drawable.no_image);
        }


    }

    // Return the size of your itemsData (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return newsItemList.size();
    }

    public interface NewsItemOnClickListener {
        void onNewsClicked(SingleNewsItem singleNewsItem);
    }

    // inner class to hold a reference to each item of RecyclerView
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView txtViewTitle;
        public ImageView imgViewIcon;
        public ViewHolderClickListener listener;

        public ViewHolder(View itemLayoutView, ViewHolderClickListener listener) {
            super(itemLayoutView);
            this.listener = listener;
            txtViewTitle = (TextView) itemLayoutView.findViewById(R.id.item_title);
            imgViewIcon = (ImageView) itemLayoutView.findViewById(R.id.item_icon);
            itemLayoutView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onClick(v, getPosition());
        }

        public interface ViewHolderClickListener {
            void onClick(View v, int position);
        }
    }
}
