package com.bulatowf.multimediatest;

import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Handler;
import android.util.Log;

import com.bulatowf.multimediatest.interfaces.AlbumCoversInteractor;
import com.bulatowf.multimediatest.interfaces.MediaFilesInteractor;
import com.bulatowf.multimediatest.interfaces.MediaPlayerPresenter;
import com.bulatowf.multimediatest.interfaces.MediaPlayerView;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class MediaPlayerPresenterImpl implements MediaPlayerPresenter, AlbumCoversInteractor.OnLoadFinishedListener, MediaFilesInteractor.OnLoadFinisedListener {

    private static final String LOG_TAG = "MediaPlayer";

    private AlbumCoversInteractor mAlbumCoversInteractor;
    private MediaFilesInteractor mMediaFilesInteractor;
    private MediaPlayerView mMediaPlayerView;
    private MediaPlayer mMediaPlayer;

    private File[] mMediaFilesList;
    private int mCurrFileIndex = 0;

    private ScheduledExecutorService mScheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> mScheduleFuture;
    private Handler mSeekBarUpdateHandler = new Handler();

    private boolean isStoped = false;

    public MediaPlayerPresenterImpl(final MediaPlayerView mediaPlayerView) {
        this.mAlbumCoversInteractor = new AlbumCoversInteractorImpl();
        this.mMediaFilesInteractor = new MediaFilesInteractorImpl();
        this.mMediaPlayerView = mediaPlayerView;
        this.mMediaPlayer = new MediaPlayer();

        this.mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                Log.d(LOG_TAG, "onPrepared");
                if (mMediaPlayer != null) {
                    MediaPlayerPresenterImpl.this.mMediaPlayerView.setDuration(mMediaPlayer.getDuration());
                    mMediaPlayer.start();
                    scheduleSeekBarUpdate();
                }
            }
        });

        this.mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                stopSeekbarUpdate();
                MediaPlayerPresenterImpl.this.mMediaPlayerView.updateSeekBar(mMediaPlayer.getDuration());
            }
        });
    }

    @Override
    public void loadFilesList(String dir) {
        mMediaPlayerView.disableAll();
        mMediaFilesInteractor.loadFilesList(this, dir);
    }

    @Override
    public void readyToPlay() {
        mMediaPlayer.setSurface(mMediaPlayerView.getVideoSurface());
        prepareAndPlay();
    }

    private void prepareAndPlay() {
        String fileName = mMediaFilesList[mCurrFileIndex].getName();
        if (fileName.matches("^.*\\.(3gp|webm|mp4)$")) {
            mMediaPlayerView.prepareUIForVideo();
        } else if (fileName.matches("^.*\\.(mp3|wav)$")) {
            mMediaPlayerView.prepareUIForAudio();
            mAlbumCoversInteractor.loadAlbumCover(this, mMediaPlayerView.getContext().getContentResolver(), mMediaFilesList[mCurrFileIndex]);
        }
        play();
    }

    private void play() {
        if(mMediaPlayer != null) {
            try {
                isStoped = false;
                mMediaPlayer.reset();
                mMediaPlayerView.switchToPlayState();
                mMediaPlayerView.setMediaFileName(mMediaFilesList[mCurrFileIndex].getName());
                mMediaPlayer.setDataSource(mMediaFilesList[mCurrFileIndex].getAbsolutePath());
                mMediaPlayer.prepareAsync();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void scheduleSeekBarUpdate() {
        stopSeekbarUpdate();
        if (!mScheduledExecutorService.isShutdown()) {
            mScheduleFuture = mScheduledExecutorService.scheduleAtFixedRate(
                    new Runnable() {
                        @Override
                        public void run() {
                            mSeekBarUpdateHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    if(mMediaPlayer != null) {
                                        mMediaPlayerView.updateSeekBar(mMediaPlayer.getCurrentPosition());
                                    }
                                }
                            });
                        }
                    }, 0, 500, TimeUnit.MILLISECONDS);
        }
    }

    private void stopSeekbarUpdate() {
        if (mScheduleFuture != null) {
            mScheduleFuture.cancel(false);
        }
    }

    @Override
    public void playNextFile() {
        if (mCurrFileIndex == mMediaFilesList.length - 1) {
            mCurrFileIndex = 0;
        } else {
            mCurrFileIndex++;
        }
        prepareAndPlay();
    }

    @Override
    public void playPrevFile() {
        if (mCurrFileIndex == 0) {
            mCurrFileIndex = mMediaFilesList.length - 1;
        } else {
            mCurrFileIndex--;
        }
        prepareAndPlay();
    }

    @Override
    public void playPause() {
        if(mMediaPlayer != null) {
            if(mMediaPlayer.isPlaying()) {
                mMediaPlayer.pause();
                mMediaPlayerView.switchToPauseState();
            } else {
                if(isStoped) {
                    play();
                } else {
                    mMediaPlayer.start();
                }
                mMediaPlayerView.switchToPlayState();
            }
        }
    }

    @Override
    public void stopPlaying() {
        if(mMediaPlayer != null) {
            mMediaPlayer.stop();
            isStoped = true;
            mMediaPlayerView.switchToPauseState();
        }
    }

    @Override
    public void onCoverLoadFinished(Bitmap cover) {
        mMediaPlayerView.setAlbumCover(cover);
    }

    @Override
    public void onFilesLoadFinished(File[] files) {
        this.mMediaFilesList = files;
        mMediaPlayerView.enableAll();
    }

    @Override
    public void onDestroy() {
        if(mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
        stopSeekbarUpdate();
        mScheduledExecutorService.shutdown();
    }
}
