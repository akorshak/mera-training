package com.example.aleksandarmarkovic.yahoonewsfeed.components;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by aleksandar.markovic on 6/10/2015.
 */
public class ImageLoader {

    private static final String TAG = ImageLoader.class.getSimpleName();

    ExecutorService executorService;
    FileStorage fileStorage;
    Context context;

    public ImageLoader(Context context) {
        executorService = Executors.newFixedThreadPool(5);
        fileStorage = new FileStorage(context);
        this.context = context;
    }

    public void queuePhoto(String url, OnImageDownloadedListener listener) {
        executorService.submit(new PhotoDownloadJob(url, listener));
    }

    private void downloadImageFromWeb(String url, OnImageDownloadedListener listener) {
        Log.d(TAG, "Download starting");
        Log.d(TAG, "FileStorage: " + fileStorage);
        File f = fileStorage.getFile(url);
        Log.d(TAG, "We have the file");
        try {
            URL imageUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) imageUrl.openConnection();
            connection.setConnectTimeout(20000);
            connection.setReadTimeout(20000);
            connection.setInstanceFollowRedirects(true);
            InputStream is = connection.getInputStream();
            OutputStream os = new FileOutputStream(f);
            copyStream(is, os);
            listener.imageDownloaded(url, f.toURI().toString());
        } catch (MalformedURLException e) {
            Log.e(TAG, "Image url cant be processed: " + e.getLocalizedMessage());
        } catch (IOException e) {
            Log.e(TAG, "Can't open connection: " + e.getLocalizedMessage());
        }
    }

    private void copyStream(InputStream is, OutputStream os) {
        final int buffer_size = 1024;
        try {
            byte[] bytes = new byte[buffer_size];
            for (; ; ) {
                int count = is.read(bytes, 0, buffer_size);
                if (count == -1)
                    break;
                os.write(bytes, 0, count);
            }
        } catch (Exception ex) {
            Log.e(TAG, "Error when coping data to SD card: " + ex.getLocalizedMessage());
        }
    }

    private class PhotoDownloadJob implements Runnable {

        private String url;
        OnImageDownloadedListener listener;

        public PhotoDownloadJob(String url,OnImageDownloadedListener listener) {
            this.url = url;
            this.listener = listener;
        }

        @Override
        public void run() {
            if (url == null)
                return;
            downloadImageFromWeb(url, listener);
        }
    }

    public interface OnImageDownloadedListener {
        public void imageDownloaded(String url, String uri);
    }
}
