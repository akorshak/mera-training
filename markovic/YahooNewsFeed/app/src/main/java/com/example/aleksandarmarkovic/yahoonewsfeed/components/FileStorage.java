package com.example.aleksandarmarkovic.yahoonewsfeed.components;

import android.content.Context;

import java.io.File;

/**
 * Created by aleksandar.markovic on 6/10/2015.
 */
public class FileStorage {

    Context context;

    public FileStorage(Context context) {
        this.context = context;
    }

    public File getFile(String url) {
        String filename = String.valueOf(url.hashCode());
        File f = new File(context.getExternalFilesDir(null), filename);
        return f;
    }

}
