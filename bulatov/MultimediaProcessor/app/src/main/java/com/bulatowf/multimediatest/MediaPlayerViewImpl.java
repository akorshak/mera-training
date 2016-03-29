package com.bulatowf.multimediatest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bulatowf.multimediatest.interfaces.MediaPlayerPresenter;
import com.bulatowf.multimediatest.interfaces.MediaPlayerView;

public class MediaPlayerViewImpl extends Fragment implements MediaPlayerView, TextureView.SurfaceTextureListener, View.OnClickListener {

    private final static String LOG_TAG = "MediaPlayer";
    private final static String MEDIA_FOLDER = "mediatest";

    private TextView mFileName;
    private SeekBar mSeekBar;
    private Button mPrevButton;
    private Button mNextButton;
    private Button mPlayButton;
    private Button mStopButton;
    private ImageView mCoverImage;
    private TextureView mTextureSurface;

    private MediaPlayerPresenter mMediaPlayerPresenter;
    private Surface mVideoSurface;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflatedView = inflater.inflate(R.layout.media_player_layout, container, false);

        this.mFileName = (TextView) inflatedView.findViewById(R.id.tv_file_name);
        this.mSeekBar = (SeekBar) inflatedView.findViewById(R.id.seek_bar);
        this.mPrevButton = (Button) inflatedView.findViewById(R.id.b_prev);
        this.mNextButton = (Button) inflatedView.findViewById(R.id.b_next);
        this.mPlayButton = (Button) inflatedView.findViewById(R.id.b_play_pause);
        this.mStopButton = (Button) inflatedView.findViewById(R.id.b_stop);
        this.mCoverImage = (ImageView) inflatedView.findViewById(R.id.iv_cover_image);
        this.mTextureSurface = (TextureView) inflatedView.findViewById(R.id.sv_video_surface);

        return inflatedView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mMediaPlayerPresenter = new MediaPlayerPresenterImpl(this);

        mSeekBar.setOnClickListener(this);
        mPrevButton.setOnClickListener(this);
        mNextButton.setOnClickListener(this);
        mPlayButton.setOnClickListener(this);
        mStopButton.setOnClickListener(this);
        mTextureSurface.setOnClickListener(this);
        mTextureSurface.setSurfaceTextureListener(this);

        mMediaPlayerPresenter.loadFilesList(MEDIA_FOLDER);
    }

    @Override
    public Context getContext() {
        return getActivity();
    }

    @Override
    public Surface getVideoSurface() {
        return this.mVideoSurface;
    }

    @Override
    public void setAlbumCover(Bitmap cover) {
        if(mCoverImage.getVisibility() == View.VISIBLE) {
            mCoverImage.setImageBitmap(cover);
        }
    }

    @Override
    public void prepareUIForAudio() {
        mTextureSurface.setVisibility(View.GONE);
        mCoverImage.setVisibility(View.VISIBLE);
    }

    @Override
    public void prepareUIForVideo() {
        mTextureSurface.setVisibility(View.VISIBLE);
        mCoverImage.setVisibility(View.GONE);
    }

    @Override
    public void switchToPauseState() {
        mPlayButton.setText("play");
    }

    @Override
    public void switchToPlayState() {
        mPlayButton.setText("pause");
    }

    @Override
    public void updateSeekBar(int position) {
        mSeekBar.setProgress(position);
    }

    @Override
    public void setDuration(int duration) {
        mSeekBar.setMax(duration);
    }

    @Override
    public void setMediaFileName(String fileName) {
        mFileName.setText(fileName);
    }

    @Override
    public void disableAll() {
        setAvailability(false);
    }

    @Override
    public void enableAll() {
        setAvailability(true);
    }

    private void setAvailability(boolean isEnabled) {
        mSeekBar.setEnabled(isEnabled);
        mPrevButton.setEnabled(isEnabled);
        mNextButton.setEnabled(isEnabled);
        mPlayButton.setEnabled(isEnabled);
        mStopButton.setEnabled(isEnabled);
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        this.mVideoSurface = new Surface(surface);
        mMediaPlayerPresenter.readyToPlay();
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        mMediaPlayerPresenter.onDestroy();
        return false;
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {}

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {}

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.b_stop:
                mMediaPlayerPresenter.stopPlaying();
                break;
            case R.id.b_play_pause:
                mMediaPlayerPresenter.playPause();
                break;
            case R.id.b_next:
                mMediaPlayerPresenter.playNextFile();
                break;
            case R.id.b_prev:
                mMediaPlayerPresenter.playPrevFile();
                break;
        }
    }
}
