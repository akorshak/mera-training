package com.example.ibulatov.navigator.interfaces;

import android.app.Activity;
import android.content.Context;

import com.example.ibulatov.navigator.models.Route;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public interface RoutesView {

    Context getContext();

    void showRoutes(List<Route> routes);
    void showMarkerPoint(LatLng point, int markerTitleRes);
    void clearMap();

    void showError(String message);

}
