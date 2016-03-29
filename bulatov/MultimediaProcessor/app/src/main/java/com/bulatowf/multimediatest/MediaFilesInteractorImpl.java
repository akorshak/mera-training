package com.bulatowf.multimediatest;

import android.os.Environment;
import com.bulatowf.multimediatest.interfaces.MediaFilesInteractor;

import java.io.File;

public class MediaFilesInteractorImpl implements MediaFilesInteractor {

    @Override
    public void loadFilesList(OnLoadFinisedListener listener, String dir) {
        listener.onFilesLoadFinished(new File(Environment.getExternalStorageDirectory(), dir).listFiles());
    }
}
