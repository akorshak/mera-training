package tng.fedorov.simplemultimediaprocessor;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainFragment extends Fragment {

    private View mView;
    private ImageView mImageView;
    private Button mButtonStart;
    private Button mButtonStop;
    private Button mButtonChose;
    private EditText mEditText;
    private MediaPlayer mPlayer;
    private ArrayList<String> mImagesList;
    private ArrayList<String> mAudioList;
    private int mImageNum=0;
    private int mSoundNum=0;
    private Runnable mRunnableSound;
    private AnimatorSet mAnimatorSet;
    private Thread mSoundThread;
    private long mDuration;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_main, container, false);
        mImageView = (ImageView) mView.findViewById(R.id.imageView);
        mButtonStart = (Button) mView.findViewById(R.id.buttonStart);
        mButtonStop = (Button) mView.findViewById(R.id.buttonStop);
        mButtonChose = (Button) mView.findViewById(R.id.buttonChose);
        mEditText = (EditText) mView.findViewById(R.id.editText);
        mButtonChose.setEnabled(true);
        mButtonStart.setEnabled(false);
        mButtonStop.setEnabled(false);
        return mView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mAnimatorSet = (AnimatorSet) AnimatorInflater.loadAnimator(getActivity(),R.animator.animator);
        mAnimatorSet.setTarget(mImageView);
        mAnimatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mImageView.setImageURI(Uri.parse(mImagesList.get(mImageNum)));
                mImageNum++;
                if (mImageNum >= mImagesList.size()) {
                    mImageNum = 0;
                }
                mAnimatorSet.start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });

        mButtonChose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File path = new File(Environment.getExternalStorageDirectory(), "test");
                File[] files = path.listFiles();
                mImagesList = new ArrayList<String>();
                mAudioList = new ArrayList<String>();
                for (File f : files) {
                    if (f.isFile() && f.getName().matches("^.*\\.(jpg|jpeg|png|bmp)$")) {
                        mImagesList.add(f.getAbsolutePath());
                    }
                    if (f.isFile() && f.getName().matches("^.*\\.(mp3|wav)$")) {
                        mAudioList.add(f.getAbsolutePath());
                    }
                }
                mButtonChose.setEnabled(true);
                mButtonStart.setEnabled(true);
                mButtonStop.setEnabled(false);
            }
        });

        mButtonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEditText.getText() != null && mEditText.getText().length() > 0) {
                    mDuration = Long.parseLong(mEditText.getText().toString());
                } else {
                    mDuration = 1;
                }
                mAnimatorSet.setDuration(mDuration * 1000);
                mAnimatorSet.start();
                mSoundThread = new Thread(mRunnableSound);
                mSoundThread.start();
                mButtonChose.setEnabled(false);
                mButtonStart.setEnabled(false);
                mButtonStop.setEnabled(true);
            }
        });

        mButtonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopAll();
                mButtonChose.setEnabled(true);
                mButtonStart.setEnabled(true);
                mButtonStop.setEnabled(false);
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        mPlayer = new MediaPlayer();
        mRunnableSound = new Runnable() {
            public void run() {
                startPlaySound();
            }
        };

        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mPlayer.reset();
                mSoundThread.interrupt();
                mSoundThread = new Thread(mRunnableSound);
                mSoundThread.start();
            }
        });
    }

    @Override
    public void onPause() {
        stopAll();
        super.onPause();
    }


    @Override
    public void onDestroyView() {
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
        super.onDestroyView();
    }

    private void stopAll() {
        mAnimatorSet.cancel();
        if (mPlayer != null) {
            if (mPlayer.isPlaying()) {
                mPlayer.stop();
            }
            mPlayer.reset();
        }
        if (mSoundThread!=null) {
            mSoundThread.interrupt();
        }
    }

    private void startPlaySound() {
        mPlayer.setLooping(false);
        mPlayer.setVolume(100, 100);
        try {
            mPlayer.setDataSource(mAudioList.get(mSoundNum));
            mPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mSoundNum++;
        if (mSoundNum >= mAudioList.size()) {
            mSoundNum = 0;
        }
        mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mPlayer.start();
            }
        });
    }

}

