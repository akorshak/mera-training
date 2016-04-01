package com.example.ibulatov.navigator.models;

import com.google.gson.annotations.SerializedName;

public class Step {

    @SerializedName("start_location")
    private Location startLocation = new Location();

    @SerializedName("end_location")
    private Location endLocation = new Location();

    @SerializedName("travel_mode")
    private String travelMode = "";

    @SerializedName("polyline")
    private Polyline polyline = new Polyline();

    public Location getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(Location startLocation) {
        this.startLocation = startLocation;
    }

    public Location getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(Location endLocation) {
        this.endLocation = endLocation;
    }

    public String getTravelMode() {
        return travelMode;
    }

    public void setTravelMode(String travelMode) {
        this.travelMode = travelMode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Step step = (Step) o;

        if (startLocation != null ? !startLocation.equals(step.startLocation) : step.startLocation != null)
            return false;
        if (endLocation != null ? !endLocation.equals(step.endLocation) : step.endLocation != null)
            return false;
        return !(travelMode != null ? !travelMode.equals(step.travelMode) : step.travelMode != null);

    }

    @Override
    public int hashCode() {
        int result = startLocation != null ? startLocation.hashCode() : 0;
        result = 31 * result + (endLocation != null ? endLocation.hashCode() : 0);
        result = 31 * result + (travelMode != null ? travelMode.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Step{" +
                "startLocation=" + startLocation +
                ", endLocation=" + endLocation +
                ", travelMode='" + travelMode + '\'' +
                ", polyline=" + polyline +
                '}';
    }
}

