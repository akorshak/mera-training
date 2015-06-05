package com.example.aleksandarmarkovic.yahoonewsfeed.database;

import java.util.Date;

/**
 * Created by aleksandar.markovic on 6/5/2015.
 */
public class SingleNewsItem {
    private String title;
    private String description;
    private Date publicationDate;
    private String url;
    private String imageURL;
    private String imageURI;

    public SingleNewsItem(String title, String description, Date publicationDate, String url, String imageURL) {
        this.title = title;
        this.description = description;
        this.publicationDate = publicationDate;
        this.url = url;
        this.imageURL = imageURL;
    }


}
