package com.bulatowf.multimediatest;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;

import com.bulatowf.multimediatest.interfaces.AlbumCoversInteractor;

import java.io.File;
import java.io.IOException;

public class AlbumCoversInteractorImpl implements AlbumCoversInteractor {

    @Override
    public void loadAlbumCover(final OnLoadFinishedListener listener, final ContentResolver resolver, final File mediaFile) {

        new Handler().post(new Runnable() {
            @Override
            public void run() {

                Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                String selection = MediaStore.Audio.Media.DATA;
                String[] selectionArgs = { mediaFile.getAbsolutePath() };
                String[] projection = {MediaStore.Audio.Media.ALBUM_ID};
                String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";

                Cursor cursor = null;

                try {
                    cursor = resolver.query(uri, projection, selection + "=?", selectionArgs, sortOrder);
                    long albumId = -1;

                    if (cursor != null && cursor.getCount() > 0) {
                        cursor.moveToFirst();
                        int idIndex = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID);
                        albumId = Long.parseLong(cursor.getString(idIndex));
                    }

                    if (albumId != -1) {
                        Uri artUri = Uri.parse("content://media/external/audio/albumart");
                        Uri albumArtUri = ContentUris.withAppendedId(artUri, albumId);

                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(resolver, albumArtUri);
                        listener.onCoverLoadFinished(bitmap);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if(cursor != null) {
                        cursor.close();
                    }
                }
            }
        });
    }
}
