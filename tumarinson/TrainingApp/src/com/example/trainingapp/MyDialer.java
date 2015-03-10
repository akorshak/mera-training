package com.example.trainingapp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class MyDialer extends Activity {

	EditText telEt;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_dialer);
		telEt = (EditText) findViewById(R.id.etPhone);
		
		Intent intent = getIntent();
		String data = intent.getData().toString();
		
		telEt.setText(data.substring(4));
	}
	
	public void onClickCall(View v) {
		Intent intent = new Intent(Intent.ACTION_CALL);
		intent.setData(Uri.parse("tel:" + telEt.getText().toString()));
		startActivity(intent);
	}
}
