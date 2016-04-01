package com.example.ibulatov.navigator.models;


import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class RoutesResponse {

    @SerializedName("routes")
    private List<Route> routes = new ArrayList<>();

    public List<Route> getRoutes() {
        return routes;
    }

    public void setRoutes(List<Route> routes) {
        this.routes = routes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RoutesResponse that = (RoutesResponse) o;

        return !(routes != null ? !routes.equals(that.routes) : that.routes != null);

    }

    @Override
    public int hashCode() {
        return routes != null ? routes.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "RoutesResponse{" +
                "routes=" + routes +
                '}';
    }
}

