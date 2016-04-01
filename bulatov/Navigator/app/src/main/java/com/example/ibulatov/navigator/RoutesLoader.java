package com.example.ibulatov.navigator;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.example.ibulatov.navigator.models.RoutesResponse;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import java.io.IOException;

public class RoutesLoader extends AsyncTaskLoader<RoutesResponse> {

    private static final String LOG_TAG = "RoutesLoader";

    private LatLng mStart;
    private LatLng mEnd;

    public RoutesLoader(Context context, LatLng start, LatLng end) {
        super(context);

        mStart = start;
        mEnd = end;
    }

    private String generateRoutesApiRequest() {
        return new StringBuilder().append("https://maps.googleapis.com/maps/api/directions/json?")
                .append("origin=").append(mStart.latitude).append(",").append(mStart.longitude)
                .append("&destination=").append(mEnd.latitude).append(",").append(mEnd.longitude)
                .append("&departure_time=").append("1459483697555")
                .append("&mode=").append("driving")
                .append("&traffic_model=").append("optimistic")
                .append("&alternatives=").append("true")
                .append("&key=").append("AIzaSyDtcEAatyHQH-AYFeXKTcFY_gxVGnv2ScA")
                .toString();
    }

    @Override
    public RoutesResponse loadInBackground() {

        String apiRequest = generateRoutesApiRequest();

        try {
            String json = new Fetcher().fetch(apiRequest);
            RoutesResponse response = new Gson().fromJson(json, RoutesResponse.class);
            return response;

        } catch (IOException e) {
            Log.e(LOG_TAG, e.getMessage());
        }

        return null;
    }



}
