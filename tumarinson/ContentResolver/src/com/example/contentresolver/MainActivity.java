package com.example.contentresolver;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class MainActivity extends Activity {

	final Uri CONTACT_URI = Uri.parse("content://com.example.contentprovider/contacts");

	final String CONTACT_NAME = "name";
	final String CONTACT_EMAIL = "email";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		ContentResolver cr = getContentResolver();
		Cursor cursor = null;
		try {
			cursor = cr.query(CONTACT_URI, null, null, null, null);
		} catch (Exception ex) {
			Toast.makeText(this,
					"Error: " + ex.getClass() + ", " + ex.getMessage(),
					Toast.LENGTH_LONG).show();
		}
		startManagingCursor(cursor);

		String from[] = { "name", "email" };
		int to[] = { android.R.id.text1, android.R.id.text2 };
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
				android.R.layout.simple_list_item_2, cursor, from, to);

		ListView lvContact = (ListView) findViewById(R.id.lvContact);
		lvContact.setAdapter(adapter);
	}

	public void onClickInsert(View v) {
		ContentValues cv = new ContentValues();
		cv.put(CONTACT_NAME, "name 4");
		cv.put(CONTACT_EMAIL, "email 4");
		Uri newUri = getContentResolver().insert(CONTACT_URI, cv);
	}

	public void onClickUpdate(View v) {
		ContentValues cv = new ContentValues();
		cv.put(CONTACT_NAME, "name 5");
		cv.put(CONTACT_EMAIL, "email 5");
		Uri uri = ContentUris.withAppendedId(CONTACT_URI, 2);
		int cnt = getContentResolver().update(uri, cv, null, null);
	}

	public void onClickDelete(View v) {
		Uri uri = ContentUris.withAppendedId(CONTACT_URI, 3);
		int cnt = getContentResolver().delete(uri, null, null);
	}

	public void onClickError(View v) {
		Uri uri = Uri.parse("content://com.example.contentprovider/phones");
		try {
			Cursor cursor = getContentResolver().query(uri, null, null, null, null);
		} catch (Exception ex) {
			Toast.makeText(this, "Error: " + ex.getClass() + ", " + ex.getMessage(), Toast.LENGTH_LONG).show();
		}

	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
