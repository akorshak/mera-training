package tng.fedorov.mycontentprovider;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by fedorov on 15.10.2015.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB = "db";
    private static final int DB_VERSION = 9;

    static final String TABLE = "data";

    static final String ID = "_id";
    static final String NAME = "name";

    private static final String CREATE_DB = "create table " + TABLE +
            "(" + ID + " integer primary key autoincrement, " + NAME + " text not null);";

    public DatabaseHelper(Context context) {
        super(context, DB, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_DB);
        ContentValues contentValues = new ContentValues();
        for (int i = 1; i <= 5; i++) {
            contentValues.put(NAME, "name " + i);
            db.insert(TABLE, null, contentValues);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < newVersion ) {
            db.beginTransaction();
            try {
                db.execSQL("drop table if exists " + TABLE + ";");
                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
            onCreate(db);
        }
    }
}
