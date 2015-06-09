package com.example.aleksandarmarkovic.yahoonewsfeed.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.aleksandarmarkovic.yahoonewsfeed.database.YahooNewsFeedContract.NewsEntry;

/**
 * Created by aleksandar.markovic on 6/5/2015.
 */
public class YahooNewsFeedDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "YahooNewsFeedDatabase";

    private static final String TEXT_TYPE = " TEXT";
    private static final String INT_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + NewsEntry.TABLE_NAME + " (" +
                    NewsEntry._ID + INT_TYPE + " PRIMARY KEY," +
                    NewsEntry.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP +
                    NewsEntry.COLUMN_NAME_DESCRIPTION + TEXT_TYPE + COMMA_SEP +
                    NewsEntry.COLUMN_NAME_PUB_DAT + " DATE" + COMMA_SEP +
                    NewsEntry.COLUMN_NAME_URL + TEXT_TYPE + COMMA_SEP +
                    NewsEntry.COLUMN_NAME_IMAGE_URL + TEXT_TYPE + COMMA_SEP +
                    NewsEntry.COLUMN_NAME_IMAGE_SD_URI + TEXT_TYPE + COMMA_SEP +
                    NewsEntry.COLUMN_NAME_IMAGE_WIDTH + INT_TYPE + COMMA_SEP +
                    NewsEntry.COLUMN_NAME_IMAGE_HEIGHT + INT_TYPE + COMMA_SEP +
                    NewsEntry.COLUMN_NAME_IMAGE_TYPE + TEXT_TYPE + COMMA_SEP +
                    " UNIQUE( " +
                    NewsEntry.COLUMN_NAME_URL + COMMA_SEP +
                    NewsEntry.COLUMN_NAME_PUB_DAT +
                    ")ON CONFLICT ABORT" +
            " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + NewsEntry.TABLE_NAME;

    public YahooNewsFeedDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }


}
