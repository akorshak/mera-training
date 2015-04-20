package com.training.android.newregistrator;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.os.Bundle;
import android.util.Xml;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class RegistratorActivity extends Activity {

    private String registratorFileName;
    private FileOutputStream outputStream = null;
    private XmlSerializer serializer;
    private int xmlRow;

    private TextView mLabelLatitude;
    private TextView mLabelLongitude;

    private ListView mSpeciesList;
    private NumberPicker mNumberAdult;

    private Button mButtonEnd;
    private Button mButtonSubmit;

    private int selectedSpecies = -1;

    private Date fileDate;

    private Handler handler;

    private RadioButton mRadioGPS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrator);

        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setDisplayUseLogoEnabled(true);
            actionBar.setSubtitle("Count your crows");
            actionBar.setTitle("Crow registrator");
            actionBar.setHomeButtonEnabled(true);
        }

        mLabelLatitude = (TextView)findViewById(R.id.label_latitude);
        mLabelLongitude = (TextView)findViewById(R.id.label_longitude);
        LocationManager locationManager = (LocationManager)getBaseContext().getSystemService(Context.LOCATION_SERVICE);
        Boolean gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (gpsEnabled) {
            Criteria criteria = new Criteria();
            String bestProvider = LocationManager.NETWORK_PROVIDER;
            //String bestProvider = locationManager.getBestProvider(criteria, false);
            Location location = locationManager.getLastKnownLocation(bestProvider);
            LocationListener loc_listener = new LocationListener() {

                public void onLocationChanged(Location l) {
                    try {
                        mLabelLatitude.setText(String.valueOf(l.getLatitude()));
                        mLabelLongitude.setText(String.valueOf(l.getLongitude()));
                        mRadioGPS.setChecked(true);
                        Handler h = new Handler();
                        h.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mRadioGPS.setChecked(false);
                            }
                        }, 10000);
                    } catch (NullPointerException e) {
                        mLabelLatitude.setText(getResources().getString(R.string.message_GPS));
                        mLabelLongitude.setText(getResources().getString(R.string.message_GPS));
                    }
                    if (selectedSpecies != -1)
                        mSpeciesList.setSelection(selectedSpecies);
                }

                public void onProviderEnabled(String p) {}

                public void onProviderDisabled(String p) {}

                public void onStatusChanged(String p, int status, Bundle extras) {}
            };
            locationManager
                    .requestLocationUpdates(bestProvider, 0, 0, loc_listener);
            location = locationManager.getLastKnownLocation(bestProvider);
            try {
                mLabelLatitude.setText(String.valueOf(location.getLatitude()));
                mLabelLongitude.setText(String.valueOf(location.getLongitude()));
                mRadioGPS.setChecked(true);
                Handler h = new Handler();
                h.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mRadioGPS.setChecked(false);
                    }
                }, 10000);
            } catch (NullPointerException e) {
                mLabelLatitude.setText(getResources().getString(R.string.message_GPS));
                mLabelLongitude.setText(getResources().getString(R.string.message_GPS));
            }
        }
        else
            Toast.makeText(getApplicationContext(),getResources().getString(R.string.message_GPS),Toast.LENGTH_SHORT).show();

        mSpeciesList = (ListView)findViewById(R.id.species_list);
        Bundle b = this.getIntent().getExtras();
        String[] array = b.getStringArray(StartActivity.BUNDLE_SPECIES_ARRAY_KEY);
        ArrayAdapter<String> speciesAdapter = new MyAdapter(this, array);
        mSpeciesList.setAdapter(speciesAdapter);
        mSpeciesList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedSpecies = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mNumberAdult = (NumberPicker)findViewById(R.id.number_adult);
        mNumberAdult.setMinValue(1);
        mNumberAdult.setMaxValue(10);
        mNumberAdult.setFocusable(false);

        mButtonEnd = (Button)findViewById(R.id.button_end);
        mButtonEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(RegistratorActivity.this)
                        .setTitle("End registration")
                        .setMessage("Are you sure you want it?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                if (outputStream != null) {
                                    try {
                                        serializer.endTag(null, "root");
                                        serializer.endDocument();
                                        serializer.flush();

                                        outputStream.close();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                                finish();
                            }})
                        .setNegativeButton(android.R.string.no, null).show();
            }
        });

        mButtonSubmit = (Button)findViewById(R.id.button_submit);
        mButtonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddXmlRow(false);
            }
        });

        SimpleDateFormat formatter = new SimpleDateFormat("_yyyy_MM_dd_HH_mm_ss");
        fileDate = new Date();
        registratorFileName = getResources().getString(R.string.title_activity_start) + formatter.format(fileDate);

        xmlRow = 1;
        if (outputStream == null) {
            try {
                File file = new File(getExternalFilesDir(null), registratorFileName + ".xml");
                outputStream = new FileOutputStream(file); //openFileOutput(registratorFileName, MODE_PRIVATE);
                serializer = Xml.newSerializer();
                serializer.setOutput(outputStream, "UTF-8");
                serializer.startDocument(null, Boolean.valueOf(true));
                serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);

                serializer.startTag(null, "root");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                AddXmlRow(true);

                /*
                try {
                    FileWriter fw = new FileWriter(routeFile, true);
                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    String s = formatter.format(new Date()) + " "
                            + mLabelLatitude.getText().toString() + " "
                            + mLabelLongitude.getText().toString() + "\n";
                    fw.append(s);
                    fw.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }*/

                handler.postDelayed(this, 120000);
            }
        });

        mRadioGPS = (RadioButton)findViewById(R.id.radio_gps);
        mRadioGPS.setClickable(false);
    }

    private synchronized void AddXmlRow(boolean route) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
            String now = formatter.format(fileDate);
            serializer.startTag(null, String.valueOf("z" + now));

            serializer.startTag(null, "string");
            serializer.text(String.valueOf(xmlRow));
            serializer.endTag(null, "string");
            xmlRow++;

            formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            now = formatter.format(new Date());
            serializer.startTag(null, "date");
            serializer.text(now);
            serializer.endTag(null, "date");

            serializer.startTag(null, "latitude");
            serializer.text(mLabelLatitude.getText().toString());
            serializer.endTag(null, "latitude");

            serializer.startTag(null, "longitude");
            serializer.text(mLabelLongitude.getText().toString());
            serializer.endTag(null, "longitude");

            String s = "";
            if (selectedSpecies != -1)
                s = mSpeciesList.getAdapter().getItem(selectedSpecies).toString();
            if (route)
                s = "";
            serializer.startTag(null, "color");
            serializer.text(s);
            serializer.endTag(null, "color");

            serializer.startTag(null, "number");
            s = String.valueOf(mNumberAdult.getValue());
            if (route)
                s = "";
            serializer.text(s);
            serializer.endTag(null, "number");

            formatter = new SimpleDateFormat("yyyyMMddHHmmss");
            now = formatter.format(fileDate);
            serializer.endTag(null, String.valueOf("z" + now));

            //Log.d(getCallingActivity().toString(), "Строка записана");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

}

