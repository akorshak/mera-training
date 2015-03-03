package com.example.contentprovider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.text.TextUtils;

public class MyContentProvider extends ContentProvider {

	public static final Uri CONTENT_URI = Uri.parse("content://com.example.contentprovider/contacts");

	private static final int ALLROWS = 1;
	private static final int SINGLE_ROW = 2;

	private static final UriMatcher uriMatcher;
	static {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI("com.example.contentprovider", "contacts", ALLROWS);
		uriMatcher.addURI("com.example.contentprovider", "contacts/#", SINGLE_ROW);
	}

	public static final String KEY_ID = "_id";
	public static final String KEY_COLUMN_1_NAME = "KEY_COLUMN_1_NAME";

	private MySQLiteOpenHelper myOpenHelper;
	
	@Override
	public boolean onCreate() {
		myOpenHelper = new MySQLiteOpenHelper(getContext());
		return true;
	}
	
	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
			String sortOrder) {

		switch (uriMatcher.match(uri)) {
		case ALLROWS: 
		      if (TextUtils.isEmpty(sortOrder)) {
		        sortOrder = MySQLiteOpenHelper.CONTACT_NAME + " ASC";
		      }
		      break;
		case SINGLE_ROW:
			String rowId = uri.getLastPathSegment();
			if (TextUtils.isEmpty(selection)) {
		        selection = KEY_ID + " = " + rowId;
		      } else {
		        selection = selection + " AND " + KEY_ID + " = " + rowId;
		      }
			break;
		default:
		    throw new IllegalArgumentException("Wrong URI: " + uri);
		}
		
		SQLiteDatabase db = myOpenHelper.getWritableDatabase();
		Cursor cursor = db.query(MySQLiteOpenHelper.CONTACT_TABLE, projection, selection,
				selectionArgs, null, null, sortOrder);
		cursor.setNotificationUri(getContext().getContentResolver(), CONTENT_URI);
		return cursor;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		if (uriMatcher.match(uri) != ALLROWS)
			throw new IllegalArgumentException("Wrong URI: " + uri);
		
		SQLiteDatabase db = myOpenHelper.getWritableDatabase();
		long rowID = db.insert(MySQLiteOpenHelper.CONTACT_TABLE, null, values);
		
		if (rowID > -1) {
			Uri resultUri = ContentUris.withAppendedId(CONTENT_URI, rowID);
			getContext().getContentResolver().notifyChange(resultUri, null);
			return resultUri;
		} else {
			return null;
		}
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		switch (uriMatcher.match(uri)) {
		case SINGLE_ROW:
			String rowId = uri.getLastPathSegment();
			if (TextUtils.isEmpty(selection)) {
				selection = MySQLiteOpenHelper.CONTACT_ID + " = " + rowId;
			} else {
				selection = selection + " AND " + MySQLiteOpenHelper.CONTACT_ID
						+ " = " + rowId;
			}
			break;
		default:
			throw new IllegalArgumentException("Wrong URI: " + uri);
		}

		SQLiteDatabase db = myOpenHelper.getWritableDatabase();
		int deleteCount = db.delete(MySQLiteOpenHelper.CONTACT_TABLE,
				selection, selectionArgs);
		getContext().getContentResolver().notifyChange(uri, null);
		return deleteCount;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		switch (uriMatcher.match(uri)) {
		case SINGLE_ROW:
			String rowId = uri.getLastPathSegment();
			if (TextUtils.isEmpty(selection)) {
				selection = MySQLiteOpenHelper.CONTACT_ID + " = " + rowId;
			} else {
				selection = selection + " AND " + MySQLiteOpenHelper.CONTACT_ID
						+ " = " + rowId;
			}
			break;
		default:
			throw new IllegalArgumentException("Wrong URI: " + uri);
	    }
		SQLiteDatabase db = myOpenHelper.getWritableDatabase();
	    int updateCount = db.update(MySQLiteOpenHelper.CONTACT_TABLE, values, selection, selectionArgs);
	    getContext().getContentResolver().notifyChange(uri, null);
	    return updateCount;
	}
	
	@Override
	public String getType(Uri uri) {
		switch (uriMatcher.match(uri)) {
		case ALLROWS:
			return "vnd.android.cursor.dir/vnd.com.example.contentprovider.contacts";
		case SINGLE_ROW:
			return "vnd.android.cursor.item/vnd.com.example.contentprovider.contacts";
		default:
			throw new IllegalArgumentException("Unsupported URI: " + uri);
		}
	}

	private class MySQLiteOpenHelper extends SQLiteOpenHelper {

		static final String DB_NAME = "mydb";
		static final int DB_VERSION = 1;

		static final String CONTACT_TABLE = "contacts";

		static final String CONTACT_ID = "_id";
		static final String CONTACT_NAME = "name";
		static final String CONTACT_EMAIL = "email";

		static final String DB_CREATE = "create table " + CONTACT_TABLE + "("
				+ CONTACT_ID + " integer primary key autoincrement, "
				+ CONTACT_NAME + " text, " + CONTACT_EMAIL + " text" + ");";
		
		
	    public MySQLiteOpenHelper(Context context) {
	      super(context, DB_NAME, null, DB_VERSION);
	    }

	    public void onCreate(SQLiteDatabase db) {
	      db.execSQL(DB_CREATE);
	      ContentValues cv = new ContentValues();
	      for (int i = 1; i <= 3; i++) {
	        cv.put(CONTACT_NAME, "name " + i);
	        cv.put(CONTACT_EMAIL, "email " + i);
	        db.insert(CONTACT_TABLE, null, cv);
	      }
	    }

	    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	    }
	  }
}
