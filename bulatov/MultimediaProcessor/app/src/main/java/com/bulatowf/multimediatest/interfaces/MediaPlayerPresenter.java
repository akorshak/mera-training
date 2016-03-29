package com.bulatowf.multimediatest.interfaces;

import android.net.Uri;

public interface MediaPlayerPresenter {
    void loadFilesList(String dir);
    void readyToPlay();
    void playNextFile();
    void playPrevFile();
    void stopPlaying();
    void playPause();
    void onDestroy();
}
