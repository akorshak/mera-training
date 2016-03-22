package com.example.ibulatov.networktest;


import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;

public class YahooWeatherClient {

    private static final String LOG_TAG = "YahooWeatherClient";
    private static final String API_REQUEST = "http://weather.yahooapis.com/forecastrss?w=2052932&u=c";

    public YahooWeatherItem fetchForecast() {

        PageFetcher fetcher = new PageFetcher();

        try {

            String xml = fetcher.fetch(API_REQUEST);
            return yahooForecastParser(xml);

        } catch (IOException | XmlPullParserException e) {
            Log.d(LOG_TAG, e.getMessage());
        }

        return null;
    }

    private YahooWeatherItem yahooForecastParser(String xml) throws XmlPullParserException, IOException {

        XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
        parser.setInput(new StringReader(xml));

        String tagName = null;

        int event = parser.getEventType();

        YahooWeatherItem weatherItem = new YahooWeatherItem();

        while (event != XmlPullParser.END_DOCUMENT) {
            tagName = parser.getName();
            if (event == XmlPullParser.START_TAG) {
                if (tagName.equals("yweather:forecast")) {

                    YahooWeatherItem.Forecast forecast = new YahooWeatherItem.Forecast();

                    forecast.setConditionsCode(Integer.parseInt(parser.getAttributeValue(null, "code")));
                    forecast.setMinTemp(Integer.parseInt(parser.getAttributeValue(null, "low")));
                    forecast.setMaxTemp(Integer.parseInt(parser.getAttributeValue(null, "high")));
                    forecast.setConditions(parser.getAttributeValue(null, "text"));
                    forecast.setDate(parser.getAttributeValue(null, "date"));
                    forecast.setDay(parser.getAttributeValue(null, "day"));

                    weatherItem.addForecast(forecast);

                } else if (tagName.equals("yweather:condition")) {

                    YahooWeatherItem.Condition condition = new YahooWeatherItem.Condition();

                    condition.setCurrConditions(parser.getAttributeValue(null, "text"));
                    condition.setCurrConditionsCode(Integer.parseInt(parser.getAttributeValue(null, "code")));
                    condition.setCurrTemp(Integer.parseInt(parser.getAttributeValue(null, "temp")));
                    condition.setCurrDate(parser.getAttributeValue(null, "date"));

                    weatherItem.setCondition(condition);
                }

            }
            event = parser.next();
        }

        return weatherItem;
    }
}
