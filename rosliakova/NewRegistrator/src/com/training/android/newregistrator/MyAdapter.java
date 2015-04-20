package com.training.android.newregistrator;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class MyAdapter extends ArrayAdapter<String> {

    private final Context mContext;
    private final String[] mData;

    public MyAdapter(Context context, String[] objects) {
    	super(context, R.layout.registrator_row, objects);
        mContext = context;
        mData = objects;
    }

    static class ViewHolder {
        TextView txtItem;
        boolean selected;
        
		public boolean isSelected() {
			return selected;
		}
		public void setSelected(boolean selected) {
			this.selected = selected;
		}
    }

    @Override
    public String getItem(int i) {
        return mData[i];
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
/*    	LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.registrator_row, viewGroup, false);
        TextView textView = (TextView) rowView.findViewById(R.id.row);
        textView.setText(mData[position]);

        return rowView;
*/   	
        ViewHolder viewHolder;

        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.registrator_row, viewGroup, false);
            viewHolder = new ViewHolder();
            viewHolder.txtItem = (TextView) convertView.findViewById(R.id.row);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.txtItem.setText(getItem(position));
//      viewHolder.selected = false;

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