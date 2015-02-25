package com.example.trainingapp;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v13.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class GalleryActivity extends Activity implements OnClickListener{

	private static final String IMG_FOLDER = "images";
	private ImagePagerAdapter mSectionsPagerAdapter;
	private ViewPager mViewPager;
	private Button prevButton;
	private Button nextButton;
	
	private ArrayList<Bitmap> imageArray=new ArrayList<Bitmap>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gallery_act);

		List<String> imageNames = null;
		try {
			imageNames = getImage();
		} catch (IOException e) {
			Toast.makeText(
					this,
					"Error! Failed to read image names in assets/images.\n"
							+ e.getMessage(), Toast.LENGTH_LONG).show();
		}
		for (String img : imageNames) {
			try {
				imageArray.add(getBitmapFromAsset(img));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		mSectionsPagerAdapter = new ImagePagerAdapter(getFragmentManager());

		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
		
		prevButton = (Button) findViewById(R.id.prevBut);
		nextButton = (Button) findViewById(R.id.nextBut);

		prevButton.setOnClickListener(this);
		nextButton.setOnClickListener(this);
		
	}

	/**
	 * Getting all image names from assets folder
	 */
	private List<String> getImage() throws IOException {
		AssetManager assetManager = getAssets();
		String[] files = assetManager.list(IMG_FOLDER);
		List<String> it = Arrays.asList(files);
		return it;
	}

	/**
	 * Read bitmap image from assests
	 */
	private Bitmap getBitmapFromAsset(String strName) throws IOException {
		AssetManager assetManager = getAssets();
		InputStream istr = null;
		istr = assetManager.open(IMG_FOLDER+"/"+strName);
		Bitmap bitmap = BitmapFactory.decodeStream(istr);
		return bitmap;
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.prevBut:
			mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1);
			break;
		case R.id.nextBut:
			mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
			break;
		}
	}
	
	/**
	 * A simle pager adapter that represents ImageholderFragment objects, in
     * sequence.
	 */
	public class ImagePagerAdapter extends FragmentPagerAdapter {

		public ImagePagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			return ImageholderFragment.newInstance(position + 1);
		}

		@Override
		public int getCount() {
			return imageArray.size();
		}
	}

	/**
	 * A placeholder fragment containing a image view.
	 */
	public static class ImageholderFragment extends Fragment {

		private static final String ARG_PAGE = "section_number";
		private int mPageNumber;
		
		public static ImageholderFragment newInstance(int sectionNumber) {
			ImageholderFragment fragment = new ImageholderFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_PAGE, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}

		@Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        mPageNumber = getArguments().getInt(ARG_PAGE);
	    }
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.gallery_fr, container,
					false);
			
			ImageView imgView = (ImageView) rootView.findViewById(R.id.galImageView);
			imgView.setImageBitmap(((GalleryActivity)getActivity()).imageArray.get(mPageNumber-1));			
			return rootView;
		}
	}
}
