package tng.fedorov.picturesviewer;

import android.os.Bundle;
import android.app.Fragment;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ViewSwitcher;


public class MainFragment extends Fragment implements ViewSwitcher.ViewFactory{

    private ImageSwitcher mImageSwitcher;
    private GestureDetector mGestureDetector;
    private View mView;
    private Integer[] mImages = {R.drawable.q1, R.drawable.q2, R.drawable.q3, R.drawable.q4};
    private int mImageNumber;
    private static final String IMAGE_NUM = "image_num";

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_main, container, false);
        if (savedInstanceState==null || savedInstanceState.isEmpty()) {
            mImageNumber=0;
        } else {
            mImageNumber = savedInstanceState.getInt(IMAGE_NUM);
        }

        mImageSwitcher = (ImageSwitcher) mView.findViewById(R.id.imageSwitcher);
        mImageSwitcher.setFactory(this);
        mImageSwitcher.setImageResource(mImages[mImageNumber]);

        return mView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(IMAGE_NUM, mImageNumber);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mGestureDetector = new GestureDetector(getActivity(), new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public void onShowPress(MotionEvent e) {

            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                return false;
            }

            @Override
            public void onLongPress(MotionEvent e) {

            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if (e1.getX() > e2.getX()) {
                    nextImageNumber();
                    mImageSwitcher.setImageResource(mImages[mImageNumber]);
                }
                if (e1.getX() < e2.getX()) {
                    prevImageNumber();
                    mImageSwitcher.setImageResource(mImages[mImageNumber]);
                }
                return true;
            }
        });

        mView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return mGestureDetector.onTouchEvent(event);
            }
        });
    }

    @Override
    public View makeView() {
        ImageView imgview = new ImageView(getActivity());
        imgview.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imgview.setLayoutParams(new ImageSwitcher.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        imgview.setBackgroundColor(0xFF000000);
        return imgview;
    }

    public void nextImageNumber() {
        mImageNumber++;
        if (mImageNumber > mImages.length - 1) {
            mImageNumber = 0;
        }
    }

    public void prevImageNumber() {
        mImageNumber--;
        if (mImageNumber < 0) {
            mImageNumber = mImages.length - 1;
        }
    }
}
