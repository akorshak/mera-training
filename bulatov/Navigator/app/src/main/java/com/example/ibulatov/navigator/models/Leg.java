package com.example.ibulatov.navigator.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Leg {

    @SerializedName("distance")
    private Distance distance = new Distance();

    @SerializedName("duration")
    private Duration duration = new Duration();

    @SerializedName("steps")
    private List<Step> steps = new ArrayList<>();

    public Distance getDistance() {
        return distance;
    }

    public void setDistance(Distance distance) {
        this.distance = distance;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Leg leg = (Leg) o;

        if (distance != null ? !distance.equals(leg.distance) : leg.distance != null) return false;
        if (duration != null ? !duration.equals(leg.duration) : leg.duration != null) return false;
        return !(steps != null ? !steps.equals(leg.steps) : leg.steps != null);

    }

    @Override
    public int hashCode() {
        int result = distance != null ? distance.hashCode() : 0;
        result = 31 * result + (duration != null ? duration.hashCode() : 0);
        result = 31 * result + (steps != null ? steps.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Leg{" +
                "distance=" + distance +
                ", duration=" + duration +
                ", steps=" + steps +
                '}';
    }
}
