package com.trainings.android.registrator;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MyAdapter extends BaseAdapter {

    private Context mContext;
    private String[] mData;

    public MyAdapter(Context context, String[] objects) {
        mContext = context;
        mData = objects;
    }

    static class ViewHolder {
        TextView txtItem;
    }

    @Override
    public String getItem(int i) {
        return mData[i];
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder;
/*
        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item, viewGroup, false);
            viewHolder = new ViewHolder();
            viewHolder.txtItem = (TextView) convertView.findViewById(R.id.txtItem);
            convertView.setTag(viewHolder);
        } else {*/
            viewHolder = (ViewHolder) convertView.getTag();
//        }

        viewHolder.txtItem.setText(getItem(position));

        return convertView;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getCount() {
        return mData.length;
    }
}