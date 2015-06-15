package com.example.aleksandarmarkovic.yahoonewsfeed;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.aleksandarmarkovic.yahoonewsfeed.database.SingleNewsItem;

import java.util.List;

/**
 * Created by aleksandar.markovic on 6/15/2015.
 */
public class NewsListAdapter extends ArrayAdapter<SingleNewsItem> {

    private LayoutInflater inflater;

    public NewsListAdapter(Context context) {
        super(context, android.R.layout.simple_list_item_2);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;

        if (convertView == null) {
            view = inflater.inflate(R.layout.single_news_list_item, parent, false);
        } else {
            view = convertView;
        }

        SingleNewsItem item = getItem(position);
        //((ImageView) view.findViewById(R.id.icon)).setImageDrawable(item.getIcon());
        ((TextView) view.findViewById(R.id.text)).setText(item.getTitle());
        return view;
    }

    public void setData(List<SingleNewsItem> data) {
        clear();
        if (data != null) {
            for (int i = 0; i < data.size(); i++) {
                add(data.get(i));
            }
        }
    }
}
