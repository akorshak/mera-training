package com.example.trainingapp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ViewHolderActivity extends ListActivity {

	private final static int NUM_OF_ITEMS = 30;

	private ArrayList<String> list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getActionBar().setDisplayHomeAsUpEnabled(true);

		list = new ArrayList<String>();
		for (int i = 0; i < NUM_OF_ITEMS; i++) {
			list.add("item #" + i);
		}

		MyAdapter<String> adapter = new MyAdapter<String>(this,
				android.R.layout.simple_list_item_1, list);
		setListAdapter(adapter);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	static class ViewHolderItem {
		TextView textViewItem;
	}

	private class MyAdapter<T> extends ArrayAdapter<T> {

		public MyAdapter(Context context, int textViewResourceId,
				List<T> objects) {
			super(context, textViewResourceId, objects);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolderItem viewHolder;

			if (convertView == null) {
				convertView = getLayoutInflater().inflate(
						android.R.layout.simple_list_item_1, parent, false);

				viewHolder = new ViewHolderItem();
				viewHolder.textViewItem = (TextView) convertView
						.findViewById(android.R.id.text1);

				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolderItem) convertView.getTag();
			}

			if (getItem(position) != null) {
				viewHolder.textViewItem.setText(getItem(position).toString());
			}

			return convertView;

		}
	}

}
