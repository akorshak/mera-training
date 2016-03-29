package com.bulatowf.multimediatest.interfaces;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.Surface;

public interface MediaPlayerView {

    Context getContext();
    Surface getVideoSurface();

    void setAlbumCover(Bitmap cover);
    void prepareUIForAudio();
    void prepareUIForVideo();
    void switchToPauseState();
    void switchToPlayState();
    void updateSeekBar(int position);
    void setDuration(int duration);
    void setMediaFileName(String fileName);

    void disableAll();
    void enableAll();
}
