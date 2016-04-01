package com.example.ibulatov.navigator;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.ibulatov.navigator.interfaces.RoutesPresenter;
import com.example.ibulatov.navigator.interfaces.RoutesView;
import com.example.ibulatov.navigator.models.Leg;
import com.example.ibulatov.navigator.models.Route;
import com.example.ibulatov.navigator.models.Step;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

public class RoutesViewImpl extends AppCompatActivity implements RoutesView, OnMapReadyCallback {

    final String TAG = "RoutesViewImpl";

    private static final float NN_LAT = 56.3286700f;
    private static final float NN_LNG = 44.0020500f;
    private static final float DEF_ZOOM = 14.5f;

    private SupportMapFragment mMapFragment;
    private GoogleMap mMap;
    private RoutesPresenter mRoutesPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRoutesPresenter = new RoutesPresenterImpl(getSupportLoaderManager(), this);
        mMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mMapFragment.getMapAsync(this);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            checkPermissionAndEnableMyLocation();
        }  else {
            Toast.makeText(RoutesViewImpl.this, "Access denied", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng position = new LatLng(NN_LAT, NN_LNG);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, DEF_ZOOM));

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                mRoutesPresenter.addPoint(latLng);
            }
        });

        checkPermissionAndEnableMyLocation();
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void checkPermissionAndEnableMyLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
        } else {
            if(mMap != null) {
                mMap.setMyLocationEnabled(true);
            }
        }
    }

    public void onClickClearMap(View view) {
        mRoutesPresenter.clearRoutes();
    }

    public void onClickLoadData(View view) {
        mRoutesPresenter.loadRoutes();
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void showRoutes(List<Route> routes) {
        if(mMap != null) {

            for(Route route : routes) {

                List<LatLng> points = new ArrayList<>();

                for(Leg leg : route.getLegs()) {
                    for(Step step : leg.getSteps()) {
                        points.add(new LatLng(step.getStartLocation().getLat(), step.getStartLocation().getLng()));
                        points.add(new LatLng(step.getEndLocation().getLat(), step.getEndLocation().getLng()));
                    }
                }

                PolylineOptions polylineOptions = new PolylineOptions();
                polylineOptions.addAll(points);
                polylineOptions.geodesic(true);
                mMap.addPolyline(polylineOptions);
            }
        }
    }

    @Override
    public void showMarkerPoint(LatLng point, int markerTitleRes) {
        if(mMap != null) {
            mMap.addMarker(new MarkerOptions().position(point).title(getResources().getString(markerTitleRes)));
        }
    }

    @Override
    public void clearMap() {
        if(mMap != null) {
            mMap.clear();
        }
    }

    @Override
    public void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRoutesPresenter.onDetach();
    }
}
