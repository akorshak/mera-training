package com.bulatowf.multimediatest.interfaces;

import android.graphics.Bitmap;
import android.net.Uri;

import java.io.File;
import java.util.List;

public interface MediaFilesInteractor {
    void loadFilesList(OnLoadFinisedListener listener, String dir);

    interface OnLoadFinisedListener {
        void onFilesLoadFinished(File[] files);
    }
}
