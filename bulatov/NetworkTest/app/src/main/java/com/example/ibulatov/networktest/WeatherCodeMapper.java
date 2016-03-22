package com.example.ibulatov.networktest;

import java.util.HashMap;
import java.util.Map;

public class WeatherCodeMapper {

    private static Map<Integer, Integer> weatherCodeMap;
    static {
        weatherCodeMap = new HashMap<>();
        weatherCodeMap.put(0, R.drawable.tornado);
        weatherCodeMap.put(1, R.drawable.tropical_storm);
        weatherCodeMap.put(2, R.drawable.hurricane);
        weatherCodeMap.put(3, R.drawable.severe_thunderstorms);
        weatherCodeMap.put(4, R.drawable.thunderstorms);
        weatherCodeMap.put(5, R.drawable.mixed_rain_and_snow);
        weatherCodeMap.put(6, R.drawable.mixed_rain_and_sleet);
        weatherCodeMap.put(7, R.drawable.mixed_snow_and_sleet);
        weatherCodeMap.put(8, R.drawable.freezing_drizzle);
        weatherCodeMap.put(9, R.drawable.drizzle);
        weatherCodeMap.put(10, R.drawable.freezing_rain);
        weatherCodeMap.put(11, R.drawable.showers);
        weatherCodeMap.put(12, R.drawable.showers);
        weatherCodeMap.put(13, R.drawable.snow_flurries);
        weatherCodeMap.put(14, R.drawable.light_snow_showers);
        weatherCodeMap.put(15, R.drawable.blowing_snow);
        weatherCodeMap.put(16, R.drawable.snow);
        weatherCodeMap.put(17, R.drawable.hail);
        weatherCodeMap.put(18, R.drawable.sleet);
        weatherCodeMap.put(19, R.drawable.dust);
        weatherCodeMap.put(20, R.drawable.foggy);
        weatherCodeMap.put(21, R.drawable.haze);
        weatherCodeMap.put(22, R.drawable.smoky);
        weatherCodeMap.put(23, R.drawable.blustery);
        weatherCodeMap.put(24, R.drawable.windy);
        weatherCodeMap.put(25, R.drawable.cold);
        weatherCodeMap.put(26, R.drawable.cloudy);
        weatherCodeMap.put(27, R.drawable.mostly_cloudy_night);
        weatherCodeMap.put(28, R.drawable.mostly_cloudy_day);
        weatherCodeMap.put(29, R.drawable.partly_cloudy_night);
        weatherCodeMap.put(30, R.drawable.partly_cloudy_day);
        weatherCodeMap.put(31, R.drawable.clear_night);
        weatherCodeMap.put(32, R.drawable.sunny);
        weatherCodeMap.put(33, R.drawable.fair_night);
        weatherCodeMap.put(34, R.drawable.fair_day);
        weatherCodeMap.put(35, R.drawable.mixed_rain_and_hail);
        weatherCodeMap.put(36, R.drawable.hot);
        weatherCodeMap.put(37, R.drawable.isolated_thunderstorms);
        weatherCodeMap.put(38, R.drawable.scattered_thunderstorms);
        weatherCodeMap.put(39, R.drawable.scattered_thunderstorms);
        weatherCodeMap.put(40, R.drawable.scattered_showers);
        weatherCodeMap.put(41, R.drawable.heavy_snow);
        weatherCodeMap.put(42, R.drawable.scattered_snow_showers);
        weatherCodeMap.put(43, R.drawable.heavy_snow);
        weatherCodeMap.put(44, R.drawable.partly_cloudy);
        weatherCodeMap.put(45, R.drawable.thundershowers);
        weatherCodeMap.put(46, R.drawable.snow_showers);
        weatherCodeMap.put(47, R.drawable.isolated_thunderstorms);
        weatherCodeMap.put(3200, R.drawable.na);
    }

    public static int getWeatherImageResource(int weatherCode) {
        return weatherCodeMap.get(weatherCode);
    }

}
