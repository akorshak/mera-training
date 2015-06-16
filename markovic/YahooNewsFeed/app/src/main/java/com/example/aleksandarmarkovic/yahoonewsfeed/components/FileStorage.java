package com.example.aleksandarmarkovic.yahoonewsfeed.components;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;

/**
 * Created by aleksandar.markovic on 6/10/2015.
 */
public class FileStorage {

    private static final String TAG = FileStorage.class.getSimpleName();

    Context context;

    public FileStorage(Context context) {
        this.context = context;
    }

    public synchronized File getFile(String url) {
        File f = null;

        if (url == null)
            return f;

        if (isExternalStorageWritable()) {
            String filename = String.valueOf(Math.abs(url.hashCode()));
            Log.d(TAG, "Trying to open a new file with filename: " + filename);
            File file = context.getExternalFilesDir(null);
            if (file == null) {
                Log.e(TAG, "We cant open external files dir");
            } else {
                f = new File(context.getExternalFilesDir(null), filename);
                Log.d(TAG, "We have a new file open: " + f);
            }
        }
        return f;
    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }
}
