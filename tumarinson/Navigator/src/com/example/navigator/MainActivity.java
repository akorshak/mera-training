package com.example.navigator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity {

	private SupportMapFragment mapFragment;
	private GoogleMap map;

	private TextView tvRouteInfo;
	private ArrayList<LatLng> routePoints = new ArrayList<LatLng>();
	
	final String TAG = "PathGoogleMapActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		tvRouteInfo = (TextView)findViewById(R.id.tvRouteInfo);
		
		mapFragment = (SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map);
		map = mapFragment.getMap();
		
		if (map != null) {
			map.setMyLocationEnabled(true);
			//map.setTrafficEnabled(true);
			
			map.setOnMapLongClickListener(new OnMapLongClickListener() {
				@Override
				public void onMapLongClick(LatLng latLng) {
					routePoints.add(latLng);
					map.addMarker(new MarkerOptions().position(latLng)
							.title("Point #" + routePoints.size()));
				}
			});
		}
	}

	public void onClickButton(View view) {
		switch(view.getId()) {
		case R.id.btnGetRoute:
			if(routePoints.size()>=2) {
				String url = getMapsApiDirectionsUrl();
				ReadTask downloadTask = new ReadTask();
				downloadTask.execute(url);
			} else {
				Toast.makeText(getApplicationContext(),
						"You should have at least 2 points!", Toast.LENGTH_LONG)
						.show();
			}
			break;
		case R.id.btnClear:
			tvRouteInfo.setVisibility(TextView.GONE);
			map.setPadding(0, 0, 0, 0);
			routePoints.clear();
			map.clear();
			break;
		}
		
		
	}

	private String getMapsApiDirectionsUrl() {
		StringBuffer points=new StringBuffer();
		for(LatLng point : routePoints) {
			if(routePoints.indexOf(point) == 0) {
				points.append("origin=" + point.latitude + "," + point.longitude);
			} else if (routePoints.indexOf(point) == routePoints.size()-1) {
				points.append("&destination=" + point.latitude + "," + point.longitude);
			} else if (routePoints.indexOf(point) == 1) {
				points.append("&waypoints=optimize:true|" + point.latitude + "," + point.longitude);
			} else {
				points.append("|" + point.latitude + "," + point.longitude);
			}
		}
		String sensor = "sensor=false";
		String params = points + "&" + sensor;
		String output = "json";
		String url = "https://maps.googleapis.com/maps/api/directions/"
				+ output + "?" + params;
		return url;
	}

	private class ReadTask extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... url) {
			String data = "";
			try {
				HttpConnection http = new HttpConnection();
				data = http.readUrl(url[0]);
			} catch (Exception e) {
				Log.d("Background Task", e.toString());
			}
			return data;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			new ParserTask().execute(result);
		}
	}

	private class ParserTask extends
			AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

		@Override
		protected List<List<HashMap<String, String>>> doInBackground(
				String... jsonData) {

			JSONObject jObject;
			List<List<HashMap<String, String>>> routes = null;

			try {
				jObject = new JSONObject(jsonData[0]);
				PathJSONParser parser = new PathJSONParser();
				routes = parser.parse(jObject);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return routes;
		}

		@Override
		protected void onPostExecute(List<List<HashMap<String, String>>> routes) {
			ArrayList<LatLng> points = null;
			PolylineOptions polyLineOptions = null;

			int pathLenth=0;
			int pathTime=0;
			
			// traversing through routes
			for (int i = 0; i < routes.size(); i++) {
				points = new ArrayList<LatLng>();
				polyLineOptions = new PolylineOptions();
				List<HashMap<String, String>> path = routes.get(i);

				// get distance and durations
				pathLenth += Integer.parseInt(path.get(0).get("distance"));
				pathTime += Integer.parseInt(path.get(1).get("duration"));
				
				for (int j = 2; j < path.size(); j++) {
					HashMap<String, String> point = path.get(j);

					try {
						double lat = Double.parseDouble(point.get("lat"));
						double lng = Double.parseDouble(point.get("lng"));
						LatLng position = new LatLng(lat, lng);
		
						points.add(position);
					} catch (NullPointerException e) {
						e.printStackTrace();
					}
				}

				polyLineOptions.addAll(points);
				polyLineOptions.width(3);
				polyLineOptions.color(Color.BLUE);
			}

			String info = "Длина пути: " + pathLenth/100/10f + "км\n" +
					"Время в пути: " + (pathTime/60+1) + "мин";
			tvRouteInfo.setText(info);
			tvRouteInfo.setVisibility(TextView.VISIBLE);
			map.setPadding(0, 65, 0, 0);
			
			map.addPolyline(polyLineOptions);
		}
	}

}