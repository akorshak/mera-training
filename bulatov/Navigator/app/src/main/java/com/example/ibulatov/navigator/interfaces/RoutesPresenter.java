package com.example.ibulatov.navigator.interfaces;

import com.google.android.gms.maps.model.LatLng;

public interface RoutesPresenter {

    void loadRoutes();
    void clearRoutes();
    void addPoint(LatLng point);

    void onDetach();

}
