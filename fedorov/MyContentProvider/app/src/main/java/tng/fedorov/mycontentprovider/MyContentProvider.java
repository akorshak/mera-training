package tng.fedorov.mycontentprovider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.Toast;

/**
 * Created by fedorov on 14.10.2015.
 */
public class MyContentProvider extends ContentProvider {

    private static final String AUTHORITY = "tng.fedorov.provider";
    private static final String PATH = "items";
    private static final int URI_ID = 1;
    private static final int URI_ALL = 2;

    private SQLiteDatabase mDatabase;
    private DatabaseHelper mHelper;

    private static UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sUriMatcher.addURI(AUTHORITY,PATH + "/#",URI_ID);
        sUriMatcher.addURI(AUTHORITY,PATH,URI_ALL);
    }

    @Override
    public boolean onCreate() {
        mHelper = new DatabaseHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {

        switch (sUriMatcher.match(uri)) {
            case URI_ID:
                selection = selection + " AND " + DatabaseHelper.ID + " = " + uri.getLastPathSegment();
                break;
            case URI_ALL:
                if (sortOrder == null) {
                    sortOrder = DatabaseHelper.NAME + " ASC";
                }
                break;
            default:
                Toast.makeText(getContext(), "Incorrect URI: " + uri, Toast.LENGTH_SHORT).show();
        }
        Cursor cursor;
        mDatabase = mHelper.getWritableDatabase();
        cursor = mDatabase.query(DatabaseHelper.TABLE, projection, selection, selectionArgs,
                null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case URI_ID:
                return "vnd.android.cursor.item/vnd." + AUTHORITY + "." + PATH;
            case URI_ALL:
                return "vnd.android.cursor.dir/vnd." + AUTHORITY + "." + PATH;
            default:
                return null;
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        if (sUriMatcher.match(uri) != URI_ALL) {
            Toast.makeText(getContext(), "Incorrect URI: " + uri, Toast.LENGTH_SHORT).show();
        }
        mDatabase = mHelper.getWritableDatabase();
        long rowID = mDatabase.insert(DatabaseHelper.TABLE, null, values);
        Uri resultUri = Uri.parse("content://" + AUTHORITY + "/" + PATH + "/" + rowID);
        getContext().getContentResolver().notifyChange(resultUri, null);
        return resultUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        switch (sUriMatcher.match(uri)) {
            case URI_ID:
                if (TextUtils.isEmpty(selection)) {
                    selection = DatabaseHelper.ID + " = " + uri.getLastPathSegment();
                } else {
                    selection = selection + " AND " + DatabaseHelper.ID + " = " + uri.getLastPathSegment();
                }
                break;
            case URI_ALL:
                break;
            default:
                Toast.makeText(getContext(), "Incorrect URI: " + uri, Toast.LENGTH_SHORT).show();
        }
        mDatabase = mHelper.getWritableDatabase();
        int rowsNum = mDatabase.delete(DatabaseHelper.TABLE, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsNum;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        switch (sUriMatcher.match(uri)) {
            case URI_ID:
                if (TextUtils.isEmpty(selection)) {
                    selection = DatabaseHelper.ID + " = " + uri.getLastPathSegment();
                } else {
                    selection = selection + " AND " + DatabaseHelper.ID + " = " + uri.getLastPathSegment();
                }
                break;
            case URI_ALL:
                break;
            default:
                Toast.makeText(getContext(), "Incorrect URI: " + uri, Toast.LENGTH_SHORT).show();
        }
        mDatabase = mHelper.getWritableDatabase();
        int rowsNum = mDatabase.update(DatabaseHelper.TABLE, values, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsNum;
    }
}
