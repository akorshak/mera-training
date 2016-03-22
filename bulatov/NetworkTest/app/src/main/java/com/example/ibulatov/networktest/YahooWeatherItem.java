package com.example.ibulatov.networktest;

import java.util.ArrayList;
import java.util.List;

public class YahooWeatherItem {

    private Condition condition;
    private List<Forecast> forecastList = new ArrayList<>();

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    public Condition getCondition() {
        return this.condition;
    }

    public void addForecast(Forecast forecast) {
        this.forecastList.add(forecast);
    }

    public List<Forecast> getForecastList() {
        return this.forecastList;
    }

    public static class Condition {
        private String currConditions;
        private int currConditionsCode;
        private int currTemp;
        private String currDate;

        public String getCurrConditions() {
            return currConditions;
        }

        public void setCurrConditions(String currConditions) {
            this.currConditions = currConditions;
        }

        public int getCurrConditionsCode() {
            return currConditionsCode;
        }

        public void setCurrConditionsCode(int conditionsCode) {
            this.currConditionsCode = conditionsCode;
        }

        public int getCurrTemp() {
            return currTemp;
        }

        public void setCurrTemp(int currTemp) {
            this.currTemp = currTemp;
        }

        public String getCurrDate() {
            return currDate;
        }

        public void setCurrDate(String currDate) {
            this.currDate = currDate;
        }

    }

    public static class Forecast {
        private int minTemp;
        private int maxTemp;
        private String conditions;
        private int conditionsCode;
        private String day;
        private String date;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public int getMinTemp() {
            return minTemp;
        }

        public void setMinTemp(int minTemp) {
            this.minTemp = minTemp;
        }

        public int getMaxTemp() {
            return maxTemp;
        }

        public void setMaxTemp(int maxTemp) {
            this.maxTemp = maxTemp;
        }

        public String getConditions() {
            return conditions;
        }

        public void setConditions(String conditions) {
            this.conditions = conditions;
        }

        public int getConditionsCode() {
            return conditionsCode;
        }

        public void setConditionsCode(int conditionsCode) {
            this.conditionsCode = conditionsCode;
        }

        public String getDay() {
            return day;
        }

        public void setDay(String day) {
            this.day = day;
        }

    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("date: ").append(condition.currDate).append("\n")
                .append("temperature: ").append(condition.currTemp).append("\n")
                .append("condition: ").append(condition.currConditions).append("\n");

        for(Forecast f : forecastList) {
            sb.append("  [ ").append("date: ").append(f.date).append("\n")
                    .append("    minTemp: ").append(f.minTemp).append("\n")
                    .append("    maxTemp: ").append(f.maxTemp).append("\n")
                    .append("    condition: ").append(f.conditions).append(" ]\n");
        }
        return sb.toString();
    }

}
