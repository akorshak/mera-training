package tng.fedorov.navigator;

import android.app.LoaderManager;
import android.content.Loader;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.Random;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        LoaderManager.LoaderCallbacks<ArrayList<LatLng>> {

    private GoogleMap mMap;
    private static Polyline mPolyline;
    private static Marker mMarker;
    private LatLng mMyLatLng;
    private Loader mLoader;
    private int mId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mId = new Random().nextInt();
        mLoader=getLoaderManager().restartLoader(mId, null, MapsActivity.this);
        mapFragment.setRetainInstance(true);
        Log.d("mylog", "onCreate");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d("mylog", "onMapReady");
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        mMap.setBuildingsEnabled(true);
        mMap.setTrafficEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setAllGesturesEnabled(true);

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        try {
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            mMyLatLng = new LatLng(location.getLatitude(), location.getLongitude());
            mMap.animateCamera(CameraUpdateFactory.newLatLng(mMyLatLng));
        } catch (SecurityException e) {
            e.printStackTrace();
        }

        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                return false;
            }
        });

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (mPolyline != null) {
                    mPolyline.remove();
                }
                if (mMarker != null) {
                    mMarker.remove();
                }
                mMarker = mMap.addMarker(new MarkerOptions().position(latLng).title(getResources()
                        .getString(R.string.destination)));
                mMarker.showInfoWindow();

                Bundle bundle = new Bundle();
                bundle.putParcelable("origin", mMyLatLng);
                bundle.putParcelable("destination", latLng);
                mLoader = getLoaderManager().restartLoader(mId, bundle, MapsActivity.this);
                mLoader.forceLoad();
            }
        });
    }


    @Override
    public Loader<ArrayList<LatLng>> onCreateLoader(int id, Bundle args) {
        Log.d("mylog", "onCreateLoader");
        return new RouteCalculator(MapsActivity.this, args);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<LatLng>> loader, ArrayList<LatLng> data) {
        Log.d("mylog", "onLoadFinished");
        if (data == null || data.isEmpty()) {
            Toast.makeText(this, "No data!", Toast.LENGTH_SHORT).show();
        } else {
            PolylineOptions polylineOptions = new PolylineOptions();
            polylineOptions.addAll(data);
            polylineOptions.geodesic(true);
            mPolyline = mMap.addPolyline(polylineOptions);
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<LatLng>> loader) {
        Log.d("mylog", "onLoaderReset");
    }
}
