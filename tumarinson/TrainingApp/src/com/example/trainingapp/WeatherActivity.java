package com.example.trainingapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class WeatherActivity extends Activity {

	private static final String WEATHER_API_URL="http://informer.gismeteo.ru/xml/27553_1.xml";
	
	private static final String XML_TOWN_TAG = "TOWN";
	private static final String XML_TOWN_NAME = "sname";
	private static final String XML_FORECAST_TAG = "FORECAST";
	private static final String XML_DAY = "day";
	private static final String XML_MONTH = "month";
	private static final String XML_YEAR = "year";
	private static final String XML_TOD = "tod";
	private static final String XML_WEEKDAY = "weekday";
	private static final String XML_PHENOMENA_TAG = "PHENOMENA";
	private static final String XML_CLOUDINESS = "cloudiness";
	private static final String XML_PRECIPIATATION = "precipitation";
	private static final String XML_TEMPERATURE_TAG = "TEMPERATURE";
	private static final String XML_TEMP_MAX = "max";
	private static final String XML_TEMP_MIN = "min";
	
	private static final int OK_CODE = 0;
	private static final int NOT_CONNECTED_CODE = 1;
	private static final int PARSE_ERR_CODE = 2;
	
	private final String []weekdays_lbl={ 
			"воскресенье",
			"понедельник",
			"вторник",
			"среда",
			"четверг",
			"п€тница",
			"суббота"
	};
	
	private final String []tods_lbl={ 
			"утро",
			"день",
			"вечер",
			"ночь"
	};
	
	private final String []cloud_lbl={ 
			"€сно",
			"малооблачно",
			"облачно",
			"пасмурно"
	};
	
	private final String []prec_lbl={ 
			"дождь",
			"ливень",
			"снег",
			"снег",
			"гроза",
			"нет данных",
			"без осадков",
	};
	
	private TextView tvTown;
	
	private ListView weatherList;
	private SimpleAdapter weatherAdapter;
	
	private ArrayList<Map<String,Object>> weatherInfo = new ArrayList<Map<String, Object>>(4);
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.weather);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		tvTown=(TextView) findViewById(R.id.weath_town);
		weatherList=(ListView) findViewById(R.id.weatherlist);
		
		String[] from = { XML_DAY, XML_WEEKDAY, XML_TOD, XML_TEMPERATURE_TAG, XML_CLOUDINESS, XML_PRECIPIATATION };
		int[] to = {  R.id.weath_date, R.id.weath_weekday, R.id.weath_tod, R.id.weath_temp, R.id.weath_cloud, R.id.weath_prec };

		weatherAdapter=new SimpleAdapter(this, weatherInfo, R.layout.weather_item,
		        from, to);
	
		weatherAdapter.setViewBinder(new MyViewBinder());
		weatherList.setAdapter(weatherAdapter);
		
		tvTown.setText("Ќижний Ќовгород");
		
		WeatherLoader weatherLoader=new WeatherLoader();
		weatherLoader.execute(WEATHER_API_URL);
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

	private void processXmlMessage(String xmlMessage) throws XmlPullParserException, IOException {
		XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
	    XmlPullParser xpp = factory.newPullParser();
	    xpp.setInput(new StringReader(xmlMessage));
		
	    parseXML(xpp);
	}
	
	private void parseXML(XmlPullParser xpp) throws XmlPullParserException, IOException {
		int eventType = xpp.next();
		int forecastCount = 0;
		while (eventType != XmlPullParser.END_DOCUMENT) {
			if (eventType == XmlPullParser.START_TAG) {
				if (XML_FORECAST_TAG.equals(xpp.getName())) {
					weatherInfo.add(new HashMap<String, Object>());
					String date=""+Integer.parseInt(xpp.getAttributeValue(null, XML_DAY))
							+"."+ Integer.parseInt(xpp.getAttributeValue(null, XML_MONTH))
							+"."+Integer.parseInt(xpp.getAttributeValue(null, XML_YEAR));
					weatherInfo.get(forecastCount).put(XML_DAY, date);
					weatherInfo.get(forecastCount).put(XML_TOD,Integer
							.parseInt(xpp.getAttributeValue(null, XML_TOD)));
					weatherInfo.get(forecastCount).put(XML_WEEKDAY,Integer
							.parseInt(xpp.getAttributeValue(null, XML_WEEKDAY)));
				}
				if (XML_PHENOMENA_TAG.equals(xpp.getName())) {
					weatherInfo.get(forecastCount).put(XML_CLOUDINESS, Integer
							.parseInt(xpp.getAttributeValue(null, XML_CLOUDINESS)));
					weatherInfo.get(forecastCount).put(XML_PRECIPIATATION, Integer
							.parseInt(xpp.getAttributeValue(null, XML_PRECIPIATATION)));
				}
				if (XML_TEMPERATURE_TAG.equals(xpp.getName())) {
					weatherInfo.get(forecastCount).put(XML_TEMPERATURE_TAG,
							(Integer.parseInt(xpp.getAttributeValue(null, XML_TEMP_MAX)) + 
							Integer.parseInt(xpp.getAttributeValue(null, XML_TEMP_MIN))) / 2);
				}
			}

			if (eventType == XmlPullParser.END_TAG) {
				if (XML_FORECAST_TAG.equals(xpp.getName())) {
					forecastCount++;
				}
			}
			eventType = xpp.next();
		}
	}
	
	private class WeatherLoader extends AsyncTask<String, Void, Integer> {

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
			
			WeatherHttpClient weathClient = new WeatherHttpClient();
			
			if (checkInternetConnection()) {
				String result=weathClient.getWeatherData(url[0]);
				
				try {
					processXmlMessage(result);
					return OK_CODE;
				} catch (XmlPullParserException e) {
					return PARSE_ERR_CODE;
				} catch (IOException e) {
					return PARSE_ERR_CODE;
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
				weatherAdapter.notifyDataSetChanged();
				break;
			case NOT_CONNECTED_CODE:
				tvTown.setText("There are no internet connection");
				break;
			case PARSE_ERR_CODE:
				tvTown.setText("Can't parse JSON response");
				break;
			}
		}
	}

	class MyViewBinder implements SimpleAdapter.ViewBinder {

		@Override
		public boolean setViewValue(View view, Object data,
				String textRepresentation) {
			int i = 0;
			switch (view.getId()) {
			case R.id.weath_date:
				((TextView) view).setText(((String)data));
				return true;
			case R.id.weath_weekday:
				((TextView) view).setText(weekdays_lbl[((Integer)data).intValue()-1]);
				return true;
			case R.id.weath_tod:
				((TextView) view).setText(tods_lbl[((Integer)data).intValue()]);
				return true;
			case R.id.weath_temp:
				((TextView) view).setText("" + ((Integer)data).intValue() + " \u2103");
				return true;
			case R.id.weath_cloud:
				((TextView) view)
						.setText(cloud_lbl[((Integer)data).intValue()]);
				return true;
			case R.id.weath_prec:
				((TextView) view)
						.setText(prec_lbl[((Integer)data).intValue() - 4]);
				return true;
			default:
				return false;
			}
		}

	}	
}
