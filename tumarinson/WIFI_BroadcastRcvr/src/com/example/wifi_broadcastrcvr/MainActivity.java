package com.example.wifi_broadcastrcvr;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends Activity {

	TextView stateText;
	BroadcastReceiver br;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		stateText= (TextView)findViewById(R.id.tv1);
		
		br=new WiFiBroadcastReceiver();
		
		registerReceiver(br, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
	}
	
	@Override
	  protected void onDestroy() {
	    super.onDestroy();
	    unregisterReceiver(br);
	  }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	class WiFiBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			NetworkInfo networkInfo = (NetworkInfo) intent
					.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
			
			WifiManager myWifiManager = (WifiManager)getSystemService(Context.WIFI_SERVICE);
			WifiInfo myWifiInfo = myWifiManager.getConnectionInfo();
			
			if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
				if (networkInfo.isConnected()) {
					stateText.setText("WiFi is CONNECTED!\n"+
							"IP: "+ myWifiInfo.getIpAddress()+"\n"+
							"MAC: "+ myWifiInfo.getMacAddress()+"\n"+
							"Speed: "+ String.valueOf(myWifiInfo.getLinkSpeed()) + " "
							+ WifiInfo.LINK_SPEED_UNITS);
				} else {
					stateText.setText("WiFi is DISCONNECTED!");
				}
			}

		}

	}
}
