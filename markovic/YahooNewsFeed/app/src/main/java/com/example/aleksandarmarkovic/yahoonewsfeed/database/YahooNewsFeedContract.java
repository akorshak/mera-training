package com.example.aleksandarmarkovic.yahoonewsfeed.database;

import android.provider.BaseColumns;

/**
 * Created by aleksandar.markovic on 6/5/2015.
 */
public final class YahooNewsFeedContract {

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public YahooNewsFeedContract() {}

    /* Inner class that defines the table contents */
    public static abstract class NewsEntry implements BaseColumns {
        public static final String TABLE_NAME = "news_entry";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_PUB_DAT = "publication_date";
        public static final String COLUMN_NAME_URL = "url";
        public static final String COLUMN_NAME_IMAGE_URL = "image_url";
        public static final String COLUMN_NAME_IMAGE_HEIGHT = "image_height";
        public static final String COLUMN_NAME_IMAGE_WIDTH = "image_width";
        public static final String COLUMN_NAME_IMAGE_TYPE = "image_type";
        public static final String COLUMN_NAME_IMAGE_SD_URI = "image_sd_uri";
    }
}
