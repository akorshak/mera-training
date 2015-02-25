package com.example.trainingapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class CallDialerActivity extends Activity {

	//private final String FILE_NAME="phone_number";
	private Button dialBut;
	private EditText dialEt;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.call_dialer);
		
		dialBut = (Button) findViewById(R.id.dialBut);
		dialEt = (EditText) findViewById(R.id.dialEditText);
		
		dialBut.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					AssetManager am = getAssets();
					StringBuffer buffer = new StringBuffer();
					InputStream is = am.open(dialEt.getText().toString());

					BufferedReader br = new BufferedReader(new InputStreamReader(is));
					String line = null;
					while ((line = br.readLine()) != null)
						buffer.append(line);

					is.close();

					if (!Patterns.PHONE.matcher(buffer).matches()) {
						Toast.makeText(CallDialerActivity.this,
								buffer + " is incorrect phone number",
								Toast.LENGTH_LONG).show();
					} else {
						Intent intent = new Intent(Intent.ACTION_DIAL);
						intent.setData(Uri.parse("tel:" + buffer.toString()));
						startActivity(intent);
					}
				} catch (IOException e) {
					Toast.makeText(CallDialerActivity.this,
							e.getClass() + "\n" + e.getMessage(),
							Toast.LENGTH_LONG).show();
				}
			}
		});
		
		
	}

	
}
