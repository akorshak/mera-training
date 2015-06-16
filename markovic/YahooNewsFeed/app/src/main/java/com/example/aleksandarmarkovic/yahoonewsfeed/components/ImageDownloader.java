package com.example.aleksandarmarkovic.yahoonewsfeed.components;

import android.content.Context;
import android.util.Log;

import com.example.aleksandarmarkovic.yahoonewsfeed.BuildConfig;
import com.example.aleksandarmarkovic.yahoonewsfeed.database.SingleNewsItem;
import com.example.aleksandarmarkovic.yahoonewsfeed.utils.Constants;

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
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Created by aleksandar.markovic on 6/10/2015.
 */
public class ImageDownloader {

    private static final String TAG = ImageDownloader.class.getSimpleName();

    private ExecutorService executorService;
    private FileStorage fileStorage;

    public ImageDownloader(Context context) {
        executorService = Executors.newFixedThreadPool(5);
        fileStorage = new FileStorage(context);
    }

    /**
     * Queue photo for downloading
     *
     * @param url      - url of the photo do download
     * @param listener - listener to notify the caller when the image downloaded
     */
    public void queuePhoto(SingleNewsItem singleNewsItem, OnImageDownloadedListener listener) {
        try {
            executorService.submit(new PhotoDownloadJob(singleNewsItem, listener));
        } catch (RejectedExecutionException e) {
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "We can't accept new jobs: " + e.getLocalizedMessage());
            }
        } catch (NullPointerException e) {
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "The job you provided is null" + e.getLocalizedMessage());
            }
        }
    }

    /**
     * this function downloads the image from the given URL and writes it to the file on the SD card
     *
     * @param singleNewsItem - complete news for which we want to download an image
     * @param listener       - listener to notify when download is complete or there was some error
     */
    private void downloadImageFromWeb(SingleNewsItem singleNewsItem, OnImageDownloadedListener listener) {

        String url = singleNewsItem.getImage().getImageURL();
        Log.d(TAG, "We are trying to download new file from the " + url);
        File f = fileStorage.getFile(url);
        InputStream is = null;
        OutputStream os = null;
        Log.d(TAG, "We got the new file: " + f.toString());
        if (f != null) {
            try {
                URL imageUrl = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) imageUrl.openConnection();
                connection.setConnectTimeout(20000);
                connection.setReadTimeout(20000);
                connection.setInstanceFollowRedirects(true);
                is = connection.getInputStream();
                os = new FileOutputStream(f);
                copyStream(is, os);
                listener.imageDownloaded(singleNewsItem, f.toURI().toString());
            } catch (MalformedURLException e) {
                Log.e(TAG, "Image url cant be processed: " + e.getLocalizedMessage());
                listener.imageDownloaded(singleNewsItem, Constants.IMAGE_DOWNLOAD_ERROR);
            } catch (IOException e) {
                Log.e(TAG, "Can't open connection: " + e.getLocalizedMessage());
                listener.imageDownloaded(singleNewsItem, Constants.IMAGE_DOWNLOAD_ERROR);
            } finally {
                try {
                    if (is != null) {
                        is.close();
                    }
                } catch (IOException e) {
                    Log.e(TAG, "Error closing input stream: " + e.getLocalizedMessage());
                }
                try {
                    if (os != null) {
                        os.close();
                    }
                } catch (IOException e) {
                    Log.e(TAG, "Error closing input stream: " + e.getLocalizedMessage());
                }
            }
        } else {
            Log.e(TAG, "We could not open the file.");
            listener.imageDownloaded(singleNewsItem, Constants.IMAGE_DOWNLOAD_ERROR);
        }
    }

    /**
     * Copies data from one stream to the other stream
     *
     * @param is - Input stream from where to copy
     * @param os - Output stream to where to copy
     */
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

    /**
     * This function will wait until all the jobs currently running are not complete and then
     */
    public void waitForDownloadToFinish() {
        executorService.shutdown();
        try {
            //TODO this should be changed to wait only for certan amount of time
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            Log.e(TAG, "Error when waiting for Executor Service to complete: " + e.getLocalizedMessage());
        }
    }

    /**
     * This listener that will be used to inform the caller when the image is downloaded
     */
    public interface OnImageDownloadedListener {
        void imageDownloaded(SingleNewsItem singleNewsItem, String uri);
    }

    /**
     * Job that downloads the image from the given url
     */
    private class PhotoDownloadJob implements Runnable {

        OnImageDownloadedListener listener;
        private SingleNewsItem singleNewsItem;

        public PhotoDownloadJob(SingleNewsItem singleNewsItem, OnImageDownloadedListener listener) {
            this.singleNewsItem = singleNewsItem;
            this.listener = listener;
        }

        @Override
        public void run() {
            if (singleNewsItem == null)
                return;
            downloadImageFromWeb(singleNewsItem, listener);
        }
    }
}
