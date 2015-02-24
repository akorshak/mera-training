package com.example.trainingapp;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MainFragmentList extends ListFragment {

	private String[] listItems;
	private Class[] listActivities = {
			ImageViewActivity.class,
			ViewHolderActivity.class,
			WeatherActivity.class,
			WeatherActivityJSON.class,
			GesturesActivity.class};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.list_fragment, null);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		listItems = getActivity().getResources().getStringArray(
				R.array.list_items);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_list_item_1, listItems);
		setListAdapter(adapter);
	}

	public void onListItemClick(ListView lv, View view, int position, long id) {
		super.onListItemClick(lv, view, position, id);
		try {
			startActivity(new Intent(getActivity(), listActivities[position]));
		} catch (ArrayIndexOutOfBoundsException e) {
			Toast.makeText(getActivity(),
					"There are no activity for this item", Toast.LENGTH_SHORT)
					.show();
		} catch (Throwable e) {
			Toast.makeText(getActivity(),
					e.getClass().getName() + " " + e.getMessage(),
					Toast.LENGTH_SHORT).show();
		}
	}
}
