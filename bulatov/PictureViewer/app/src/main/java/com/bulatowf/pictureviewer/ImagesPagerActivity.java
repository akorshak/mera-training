package com.bulatowf.pictureviewer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

public class ImagesPagerActivity extends AppCompatActivity {

    private ImageLoader mImageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_pager_activity);

        ViewPager viewPager = (ViewPager) findViewById(R.id.image_viewPager);

        Bundle bundle = getIntent().getExtras();
        ArrayList<String> imagesPaths = bundle.getStringArrayList(ImagesListActivity.IMAGES_PATHS_KEY);
        int curPos = bundle.getInt(ImagesListActivity.IMAGE_CURR_POSITION_KEY);

        FragmentStatePagerAdapter imagePagerAdapter = new ImagePagerAdapter(getSupportFragmentManager(), imagesPaths);

        viewPager.setAdapter(imagePagerAdapter);
        viewPager.setCurrentItem(curPos);
    }


    // TODO: need to think
    public void loadImage(String path, ImageView imageView) {

        if(mImageLoader == null) {
            mImageLoader = new ImageLoader(imageView.getContext());
        }

        mImageLoader.loadImage(path, imageView);
    }


    public static class ImagePagerAdapter extends FragmentStatePagerAdapter {

        private ArrayList<String> mImagesPathList;

        public ImagePagerAdapter(FragmentManager fm, ArrayList<String> imagesPaths) {
            super(fm);
            this.mImagesPathList = imagesPaths;
        }

        @Override
        public int getCount() {
            return mImagesPathList.size();
        }

        @Override
        public Fragment getItem(int position) {
            return ImageFragment.newInstance(mImagesPathList.get(position));
        }
    }

    public static class ImageFragment extends Fragment {

        private static final String IMAGE_PATH_KEY = "image_path";

        private String mImagePath;
        private ImageView mImageView;

        static ImageFragment newInstance(String imagePath) {
            final ImageFragment fragment = new ImageFragment();
            final Bundle args = new Bundle();
            args.putString(IMAGE_PATH_KEY, imagePath);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            mImagePath = getArguments() != null ? getArguments().getString(IMAGE_PATH_KEY) : null;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.image_fragment, container, false);
            mImageView = (ImageView) view.findViewById(R.id.image_view);
            return view;
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);

            if (ImagesPagerActivity.class.isInstance(getActivity())) {
                ((ImagesPagerActivity) getActivity()).loadImage(mImagePath, mImageView);
            }

        }

    }






}
