package com.example.aleksandarmarkovic.yahoonewsfeed.database;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by aleksandar.markovic on 6/9/2015.
 * Class that will handle Database connection properly,
 * even when we have multiply threads running and communicating with the database.
 *
 * @see <a href="http://blog.lemberg.co.uk/concurrent-database-access">Concurrent Database Access</a>
 */
public class DatabaseManager {

    private static DatabaseManager instance;
    private static SQLiteOpenHelper databaseHelper;
    private AtomicInteger numberOfOpenConnections = new AtomicInteger();
    private SQLiteDatabase database;

    /**
     * Prevent someone from instantiating this class
     */
    public DatabaseManager() {
    }

    /**
     * We need to call this method when we want to initialize the database
     *
     * @param helper - single connection that we want to use for the database
     */
    public static synchronized void initializeInstance(SQLiteOpenHelper helper) {
        if (instance == null) {
            instance = new DatabaseManager();
            databaseHelper = helper;
        }
    }

    /**
     * Singleton getter
     *
     * @return - returns DatabaseManager singleton
     */
    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException(DatabaseManager.class.getSimpleName() +
                    " is not initialized, call initialize(..) method first.");
        }
        return instance;
    }

    /**
     * Call this method when you want to get Database for writing/reading
     *
     * @return database
     */
    public synchronized SQLiteDatabase openDatabase() {
        if (numberOfOpenConnections.incrementAndGet() == 1) {
            database = databaseHelper.getWritableDatabase();
        }
        return database;
    }

    /**
     * Call this method when you want to close the database connection
     */
    public synchronized void closeDatabase() {
        if (numberOfOpenConnections.decrementAndGet() == 0) {
            database.close();
        }
    }
}
