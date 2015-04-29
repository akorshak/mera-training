package com.training.android.newregistrator;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


public class StartActivity extends Activity {

    public static final String BUNDLE_EMAIL_STRING = "com.trainings.android.registrator.email_string";
    private String emailString = "mailto:aleros@mera.ru";

    private Button mButtonStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        try {
            BufferedReader inputReader = new BufferedReader(new InputStreamReader(
                    openFileInput("SpeciesPriority")));
            emailString = inputReader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mButtonStart = (Button) findViewById(R.id.button_start);
        mButtonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                b.putString(BUNDLE_EMAIL_STRING, emailString);
                Intent i = new Intent(StartActivity.this, RegistratorActivity.class);
                i.putExtras(b);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        FileOutputStream fos = null;
        try {
            fos = openFileOutput("SpeciesPriority", Context.MODE_PRIVATE);
            String s;
            s = emailString + "\n";
            fos.write(s.getBytes());
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
    		if (fos != null) {
            	try {
		    		fos.flush();
		    		fos.close();
            	} catch (Exception e) {
                    e.printStackTrace();
                }
    		}
        }
    }
}
