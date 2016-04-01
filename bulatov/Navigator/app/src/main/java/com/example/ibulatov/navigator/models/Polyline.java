package com.example.ibulatov.navigator.models;

import com.google.gson.annotations.SerializedName;

public class Polyline {

    @SerializedName("points")
    private String points = "";

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Polyline polyline = (Polyline) o;

        if (points != null ? !points.equals(polyline.points) : polyline.points != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        return points != null ? points.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Polyline{" +
                "points='" + points + '\'' +
                '}';
    }
}
