package com.example.ibulatov.navigator;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.example.ibulatov.navigator.interfaces.RoutesPresenter;
import com.example.ibulatov.navigator.interfaces.RoutesView;
import com.example.ibulatov.navigator.models.RoutesResponse;
import com.google.android.gms.maps.model.LatLng;

public class RoutesPresenterImpl implements RoutesPresenter, LoaderManager.LoaderCallbacks<RoutesResponse> {

    private static final int ROUTES_LOAD_TASK = 1;

    private RoutesView mRoutesView;
    private LoaderManager mLoaderManager;

    private LatLng mStart;
    private LatLng mEnd;

    public RoutesPresenterImpl(LoaderManager loaderManager, RoutesView routesView) {
        mRoutesView = routesView;
        mLoaderManager = loaderManager;
    }

    @Override
    public Loader<RoutesResponse> onCreateLoader(int id, Bundle args) {
        return new RoutesLoader(mRoutesView.getContext(), mStart, mEnd);
    }

    @Override
    public void onLoadFinished(Loader<RoutesResponse> loader, RoutesResponse data) {
        if (data != null) {
            mRoutesView.showRoutes(data.getRoutes());
        } else {
            mRoutesView.showError("Error during data loading");
        }
    }

    @Override
    public void onLoaderReset(Loader<RoutesResponse> loader) {
    }

    @Override
    public void addPoint(LatLng point) {
        if(mStart == null) {
            mStart = point;
            mRoutesView.showMarkerPoint(point, R.string.a_marker_point);
        } else if(mEnd == null) {
            mEnd = point;
            mRoutesView.showMarkerPoint(point, R.string.b_marker_point);
        }
    }

    @Override
    public void loadRoutes() {
        if (mStart != null && mEnd != null) {
            Loader loader = mLoaderManager.restartLoader(ROUTES_LOAD_TASK, null, this);
            loader.forceLoad();
        }
    }

    @Override
    public void clearRoutes() {
        mStart = null;
        mEnd = null;
        mRoutesView.clearMap();
    }

    @Override
    public void onDetach() {
        mRoutesView = null;
    }
}
