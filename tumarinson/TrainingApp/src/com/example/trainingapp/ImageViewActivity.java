package com.example.trainingapp;

import android.app.Activity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class ImageViewActivity extends Activity implements OnSeekBarChangeListener {

	private ImageView imageView;
	private SeekBar sb;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.views);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		sb = (SeekBar) findViewById(R.id.seekBar1);
		sb.setOnSeekBarChangeListener(this);
		
		LinearLayout linLayout = (LinearLayout) findViewById(R.id.LL_View);

		imageView = new ImageView(this);
		imageView.setImageResource(R.drawable.ic_launcher);
		//imageView.setBackgroundColor(R.color.margin_color);
				
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		
		int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				sb.getProgress() * 2, getResources().getDisplayMetrics());
		layoutParams.setMargins(px, px, px, px);
		
		imageView.setLayoutParams(layoutParams);
		
		linLayout.addView(imageView);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean arg2) {
		// TODO Auto-generated method stub
		
		LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams)imageView.getLayoutParams();

		int px = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, progress*2, 
				getResources().getDisplayMetrics());	
		layoutParams.setMargins(px,px,px,px);
		
		imageView.setLayoutParams(layoutParams);
	}

	@Override
	public void onStartTrackingTouch(SeekBar arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStopTrackingTouch(SeekBar arg0) {
		// TODO Auto-generated method stub
		
	}
}
