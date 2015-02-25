package com.example.trainingapp;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

public class OrientationSensorActivity extends Activity {

	private SensorManager sm;
	private TextView orientationTextView2;
	private ImageView ballIv;

	private float headingAngle=0;
	private float pitchAngle=0;
	private float rollAngle=0;
	
	int widthDisplay; 
	int heightDisplay;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.orientation_sensor);
		
		orientationTextView2 = (TextView)findViewById(R.id.orientTv2);
		ballIv = (ImageView)findViewById(R.id.ball);
		
		sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		
		widthDisplay = getWindowManager().getDefaultDisplay().getWidth(); 
		heightDisplay = getWindowManager().getDefaultDisplay().getHeight();
		
		Timer updateTimer = new Timer("gForceUpdate");
		updateTimer.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				updateGUI();
			}
		}, 0, 100);
	}

	@Override
	protected void onResume() {
		super.onResume();
		sm.registerListener(myOrientationListener,
				sm.getDefaultSensor(Sensor.TYPE_ORIENTATION),
				SensorManager.SENSOR_DELAY_NORMAL);
	}
	
	@Override
	protected void onPause() {
		sm.unregisterListener(myOrientationListener);
		super.onPause();
	}
	
	private float processCoordinate(float coord, boolean isX) {
		if (coord < 0) {
			return 0f;
		}
		if (isX && coord > widthDisplay-ballIv.getWidth()) {
			return (float) widthDisplay-ballIv.getWidth();
		}
		if (!isX && coord > heightDisplay-ballIv.getHeight()) {
			return (float) heightDisplay-ballIv.getHeight();
		}
		return coord;
	}
	
	private void updateGUI() {
		runOnUiThread(new Runnable() {
			public void run() {
				
				orientationTextView2.setText("X Pitch: "+pitchAngle+"\n"+
						"Y Roll: "+rollAngle+"\n"+
						"Z Azimuth: "+headingAngle);
				orientationTextView2.invalidate();
				
				if( Math.abs(rollAngle)>2 || Math.abs(pitchAngle)>2) {
					ballIv.setX(processCoordinate(ballIv.getX()-rollAngle * 2.5f, true));
					ballIv.setY(processCoordinate(ballIv.getY()-pitchAngle * 2.5f, false));
					ballIv.invalidate();
				}
			}
		});
	};
	
	final SensorEventListener myOrientationListener = new SensorEventListener() {
		public void onSensorChanged(SensorEvent sensorEvent) {
			if (sensorEvent.sensor.getType() == Sensor.TYPE_ORIENTATION) {
				headingAngle = (float) Math.round(sensorEvent.values[0]);
				pitchAngle = (float) Math.round(sensorEvent.values[1]);
				rollAngle = (float) Math.round(sensorEvent.values[2]);
				}
		}

		public void onAccuracyChanged(Sensor sensor, int accuracy) {
		}
	};
}
