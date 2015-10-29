package tng.fedorov.navigator;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

public class RouteCalculator extends AsyncTaskLoader<ArrayList<LatLng>>{

    private LatLng mSaddr;
    private LatLng mDaddr;

    public RouteCalculator(Context context, Bundle args) {
        super(context);
        if (args != null) {
            mSaddr = args.getParcelable("origin");
            mDaddr = args.getParcelable("destination");
        }
    }

    @Override
    public ArrayList<LatLng> loadInBackground() {
        Log.d("mylog", "loadInBackground");
        String data = null;
        if (mSaddr == null || mDaddr == null) {
            return null;
        } else {
            try {
                data = new DataDownloader().downloadData(getUrl(mSaddr, mDaddr));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return new JsonParser().getPointsList(data);
        }
    }

    private String getUrl(LatLng saddr, LatLng daddr) {
        return String.format(Locale.ENGLISH
                ,"http://maps.googleapis.com/maps/api/directions/json?origin=%f,%f&destination=%f,%f"
                ,saddr.latitude, saddr.longitude, daddr.latitude, daddr.longitude);
    }
}
