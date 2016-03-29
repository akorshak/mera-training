package com.bulatowf.multimediatest.interfaces;

import android.content.ContentResolver;
import android.graphics.Bitmap;

import java.io.File;

public interface AlbumCoversInteractor {

    ///??????
    void loadAlbumCover(OnLoadFinishedListener listener, ContentResolver resolver, File mediaFile);

    interface OnLoadFinishedListener {
        void onCoverLoadFinished(Bitmap cover);
    }
}
