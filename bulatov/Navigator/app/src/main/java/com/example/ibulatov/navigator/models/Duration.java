package com.example.ibulatov.navigator.models;

import com.google.gson.annotations.SerializedName;

public class Duration {

    @SerializedName("value")
    private int value;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Duration duration = (Duration) o;

        return value == duration.value;

    }

    @Override
    public int hashCode() {
        return value;
    }

    @Override
    public String toString() {
        return "Duration{" +
                "value=" + value +
                '}';
    }
}
