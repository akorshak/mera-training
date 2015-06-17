package com.example.aleksandarmarkovic.yahoonewsfeed;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.aleksandarmarkovic.yahoonewsfeed.database.SingleNewsItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aleksandar.markovic on 6/15/2015.
 */
public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.ViewHolder> {

    NewsItemOnClickListener newsItemOnClickListener;
    private List<SingleNewsItem> newsItemList;

    public NewsListAdapter(NewsItemOnClickListener newsItemOnClickListener) {
        this.newsItemOnClickListener = newsItemOnClickListener;
        this.newsItemList = new ArrayList<>();
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
        //viewHolder.imgViewIcon.setImageResource(newsItemList.get(position).getTitle());


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
