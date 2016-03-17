package com.example.ibulatov.emailcheckingservice.provider;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.example.ibulatov.emailcheckingservice.provider.EmailServiceContract.*;

public class EmailsProvider extends ContentProvider {

    private static final int EMAILS = 1;
    private static final int EMAIL_ID = 2;

    public static final String AUTHORITY = "com.ibulatov.providers";
    public static final String CONTACT_PATH = "emails";

    private static final UriMatcher URI_MATCHER;
    static {
        URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
        URI_MATCHER.addURI(AUTHORITY, CONTACT_PATH, EMAILS);
        URI_MATCHER.addURI(AUTHORITY, CONTACT_PATH + "/#", EMAIL_ID);
    }

    public static final Uri EMAIL_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + CONTACT_PATH);
    public static final String EMAIL_CONTENT_TYPE = "vnd.android.cursor.dir/vnd." + AUTHORITY + "." + CONTACT_PATH;
    public static final String EMAIL_CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd." + AUTHORITY + "." + CONTACT_PATH;

    private DBHelper mDBHelper;

    @Override
    public boolean onCreate() {
        mDBHelper = new DBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (URI_MATCHER.match(uri)) {
            case EMAILS:
                return EMAIL_CONTENT_TYPE;
            case EMAIL_ID:
                return EMAIL_CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }


    private String addIdSelection(String selection, String id) {
        StringBuilder queryBuilder = new StringBuilder();

        if (TextUtils.isEmpty(selection)) {
            queryBuilder.append(EmailEntry._ID).append(" = ").append(id);
        } else {
            queryBuilder.append(selection).append(" AND ").append(EmailEntry._ID).append(" = ").append(id);
        }

        return queryBuilder.toString();
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        switch (URI_MATCHER.match(uri)) {
            case EMAILS:
                break;
            case EMAIL_ID:
                selection = addIdSelection(selection, uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        Cursor cursor = mDBHelper.getReadableDatabase().query(
                EmailEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder);

        cursor.setNotificationUri(getContext().getContentResolver(), EMAIL_CONTENT_URI);

        return cursor;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        SQLiteDatabase db = mDBHelper.getWritableDatabase();

        if (URI_MATCHER.match(uri) != EMAILS) {
            throw new IllegalArgumentException("Unknown URI " + uri);
        }

        long rowId = mDBHelper.getWritableDatabase().insertWithOnConflict(EmailEntry.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_IGNORE);

        if (rowId > 0) {
            Uri insertUri = ContentUris.withAppendedId(EMAIL_CONTENT_URI, rowId);
            getContext().getContentResolver().notifyChange(uri, null);
            return insertUri;
        }

        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        switch (URI_MATCHER.match(uri)) {
            case EMAILS:
                break;
            case EMAIL_ID:
                selection = addIdSelection(selection, uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        int cnt = mDBHelper.getWritableDatabase().delete(EmailEntry.TABLE_NAME, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return cnt;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    private static class DBHelper extends SQLiteOpenHelper {

        private static final int DB_VERSION = 4;
        private static final String DB_NAME = "emails.sqlite";

        private static final String DB_CREATE_QUERY =
                "create table " + EmailEntry.TABLE_NAME + "("
                + EmailEntry._ID + " integer primary key, "
                + EmailEntry.COLUMN_NAME_EMAIL_UID + " integer not null unique, "
                + EmailEntry.COLUMN_NAME_EMAILS_SENDER + " text, "
                + EmailEntry.COLUMN_NAME_EMAILS_TEXT + " text,"
                + EmailEntry.COLUMN_NAME_EMAILS_SUBJECT + " text,"
                + EmailEntry.COLUMN_NAME_EMAILS_RECEIVED_DATE + " text"
                + ");";

        private static final String DB_DELETE_QUERY = "DROP TABLE IF EXISTS " + EmailEntry.TABLE_NAME;

        public DBHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DB_CREATE_QUERY);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(DB_DELETE_QUERY);
            onCreate(db);
        }
    }
}
