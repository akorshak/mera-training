package com.example.ibulatov.navigator.models;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Route {

    @SerializedName("legs")
    private List<Leg> legs = new ArrayList<>();

    public List<Leg> getLegs() {
        return legs;
    }

    public void setLegs(List<Leg> legs) {
        this.legs = legs;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Route route = (Route) o;

        return !(legs != null ? !legs.equals(route.legs) : route.legs != null);

    }

    @Override
    public int hashCode() {
        return legs != null ? legs.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Route{" +
                "legs=" + legs +
                '}';
    }
}
