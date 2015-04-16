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

    public static final String BUNDLE_SPECIES_ARRAY_KEY = "com.trainings.android.registrator.species_array";
    private String[] speciesArray = {
            "White crow","Black crow","Black raven"
    };

    private Button mButtonStart;

    private int selectedPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        try {
            BufferedReader inputReader = new BufferedReader(new InputStreamReader(
                    openFileInput("SpeciesPriority")));
            String inputString;
            String[] array = new String[speciesArray.length];
            int i = 0;
            while ((inputString = inputReader.readLine()) != null) {
                array[i] = inputString;
                i++;
            }
            speciesArray = array;
        } catch (IOException e) {
            e.printStackTrace();
        }

        mButtonStart = (Button) findViewById(R.id.button_start);
        mButtonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                b.putStringArray(BUNDLE_SPECIES_ARRAY_KEY, speciesArray);
                Intent i = new Intent(StartActivity.this, RegistratorActivity.class);
                i.putExtras(b);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            FileOutputStream fos = openFileOutput("SpeciesPriority", Context.MODE_PRIVATE);
            String s;
            for (int i = 0; i < speciesArray.length; i++) {
                s = speciesArray[i] + "\n";
                fos.write(s.getBytes());
            }
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
