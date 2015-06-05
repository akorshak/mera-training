package com.example.aleksandarmarkovic.yahoonewsfeed.utils;

/**
 * Created by aleksandar.markovic on 6/5/2015.
 */
public final class Constants {

    // To prevent someone from accidentally instantiating the Constants class,
    // give it an empty constructor
    public Constants() {}

    public static final String PREFERENCE_NAME = "NewsFeedPreferenceFile";
    public static final String PREFERENCE_SYNC_SETTING = "syncSettingONOFF";
    public static final String YAHOO_NEWS_REST_RSS_LINK =
            "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20rss(0%2C%2050)%20" +
                    "where%20url%3D%22http%3A%2F%2Frss.news.yahoo.com%2Frss%2Ftopstories%22" +
                    "&format=json&diagnostics=true&callback=";
}
