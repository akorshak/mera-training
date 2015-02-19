package com.example.trainingapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

public class WeatherActivityJSON extends Activity {

	private static final String URL_WEATH=
			"http://api.openweathermap.org/data/2.5/weather?units=metric&q=Nizhniy Novgorod,ru";
	private static final String URL_ICON="http://openweathermap.org/img/w/";
			
	private static final String CITY_TAG = "name";
	private static final String WEATHER_TAG = "weather";
	private static final String CLOUD_TAG = "description";
	private static final String ICON_TAG = "icon";
	private static final String MAIN_TAG = "main";
	private static final String TEMP_TAG = "temp";
	private static final String WIND_TAG = "wind";
	private static final String SPEED_TAG = "speed";
	
	private static final int OK_CODE = 0;
	private static final int NOT_CONNECTED_CODE = 1;
	private static final int PARSE_ERR_CODE = 2;
	private static final int GET_ICON_ERR_CODE = 3;
	
	private class WeatherInfo {
		String city;
		String cloud;
		int temperature;
		int wind_speed;
		String icon_id;
		byte[] icon;
	}
	
	private TextView tvTown;
	private TextView tvTemp;
	private TextView tvCloud;
	private TextView tvWind;
	private ImageView ivIcon;

	private WeatherInfo weatherInfo = new WeatherInfo();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.weater_activity_json);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		tvTown=(TextView) findViewById(R.id.w_town_j);
		tvTemp=(TextView) findViewById(R.id.w_temp_j);
	    tvCloud=(TextView) findViewById(R.id.w_cloud_j);
	    tvWind=(TextView) findViewById(R.id.w_wind_j);
	    ivIcon=(ImageView) findViewById(R.id.weath_image);
	    
	    WeatherLoader weatherLoader=new WeatherLoader();
	    weatherLoader.execute(URL_WEATH, URL_ICON);
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
	
	private void parseJSON(String data) throws JSONException {
		JSONObject jObj = new JSONObject(data);
		weatherInfo.city=jObj.getString(CITY_TAG);
		
		JSONObject weathObj = jObj.getJSONArray(WEATHER_TAG).getJSONObject(0);
		
		weatherInfo.cloud = weathObj.getString(CLOUD_TAG);
		weatherInfo.icon_id = weathObj.getString(ICON_TAG);
		 
		JSONObject mainObj = jObj.getJSONObject(MAIN_TAG);
		weatherInfo.temperature = mainObj.getInt(TEMP_TAG);
		 
		JSONObject wObj = jObj.getJSONObject(WIND_TAG);
		weatherInfo.wind_speed = wObj.getInt(SPEED_TAG);
	}
	
	private void printWeather() {
		tvTown.setText(weatherInfo.city);
		tvTemp.setText(""+weatherInfo.temperature+" \u2103");
	    tvCloud.setText(weatherInfo.cloud);
	    tvWind.setText("Wind: "+weatherInfo.wind_speed+"mps");
	    
	    Bitmap bm = BitmapFactory.decodeByteArray(weatherInfo.icon, 0, weatherInfo.icon.length);
	    ivIcon.setImageBitmap(bm);
	}
	private class WeatherLoader extends AsyncTask<String, Void, Integer> {

		WeatherHttpClient weathClient = new WeatherHttpClient();
		
		private boolean checkInternetConnection() {
			ConnectivityManager check = (ConnectivityManager) 
					getSystemService(Context.CONNECTIVITY_SERVICE);
			if (check != null) {
				NetworkInfo[] info = check.getAllNetworkInfo();
				if (info != null)
					for (int i = 0; i < info.length; i++)
						if (info[i].getState() == NetworkInfo.State.CONNECTED) {
							return true;
						}
			}
			return false;
		}
		
		@Override
		protected Integer doInBackground(String... url) {
			if (checkInternetConnection()) {

				String result = weathClient.getWeatherData(url[0]);
				
				try {
					parseJSON(result);
				} catch (JSONException e) {
					e.printStackTrace();
					return PARSE_ERR_CODE;
				}

				weatherInfo.icon = weathClient.getImage(url[1]+weatherInfo.icon_id+".png");
				if(weatherInfo.icon!=null) {
					return OK_CODE;
				} else {
					return GET_ICON_ERR_CODE;
				}
				
			} else {
				return NOT_CONNECTED_CODE;
			}
		}
		
		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
				
			switch (result) {
			case OK_CODE:
				printWeather();
				break;
			case NOT_CONNECTED_CODE:
				tvCloud.setText("There are no internet connection");
				break;
			case PARSE_ERR_CODE:
				tvCloud.setText("Can't parse JSON response");
				break;
			case GET_ICON_ERR_CODE:
				tvCloud.setText("Can't get icon from server");
				break;
			}
		}
	}
}
