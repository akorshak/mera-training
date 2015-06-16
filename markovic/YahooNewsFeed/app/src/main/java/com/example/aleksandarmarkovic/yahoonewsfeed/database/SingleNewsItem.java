package com.example.aleksandarmarkovic.yahoonewsfeed.database;

import android.content.ContentValues;

import org.parceler.Parcel;

/**
 * Created by aleksandar.markovic on 6/5/2015.
 */
@Parcel(Parcel.Serialization.BEAN)
public class SingleNewsItem {


    private String title;
    private String description;
    private String publicationDateAsString;
    private String url;
    private ImageData image;

    public SingleNewsItem(String title, String description, String publicationDateAsString, String url) {
        this.title = title;
        this.description = description;
        this.publicationDateAsString = publicationDateAsString;
        this.url = url;
    }

    public SingleNewsItem() {

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPublicationDateAsString() {
        return publicationDateAsString;
    }

    public void setPublicationDateAsString(String publicationDateAsString) {
        this.publicationDateAsString = publicationDateAsString;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ImageData getImage() {
        return image;
    }

    public void setImage(String imageType, String imageURL, int width, int height) {
        this.image = new ImageData(imageType, imageURL, width, height);
    }

    public void setImageSDCardURI(String URI) {
        if (image != null) {
            image.setImageURI(URI);
        }
    }

    @Override
    public String toString() {
        return "SingleNewsItem{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", publicationDateAsString='" + publicationDateAsString + '\'' +
                ", url='" + url + '\'' +
                ", image=" + image +
                '}';
    }

    public boolean hasPicture() {
        return image != null;
    }

    public ContentValues createContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(YahooNewsFeedContract.NewsEntry.COLUMN_NAME_TITLE, getTitle());
        contentValues.put(YahooNewsFeedContract.NewsEntry.COLUMN_NAME_DESCRIPTION, getDescription());
        contentValues.put(YahooNewsFeedContract.NewsEntry.COLUMN_NAME_URL, getUrl());
        contentValues.put(YahooNewsFeedContract.NewsEntry.COLUMN_NAME_PUB_DAT, getPublicationDateAsString());
        if (hasPicture()) {
            image.addPictureContentValues(contentValues);
        }
        return contentValues;
    }

    @Parcel(Parcel.Serialization.BEAN)
    public static class ImageData {

        private String imageType;
        private String imageURL;
        private String imageURI;
        private int height;
        private int width;

        public ImageData() {
        }

        public ImageData(String imageType, String imageURL, int width, int height) {
            this.imageType = imageType;
            this.imageURL = imageURL;
            this.width = width;
            this.height = height;
        }

        public String getImageType() {
            return imageType;
        }

        public void setImageType(String imageType) {
            this.imageType = imageType;
        }

        public String getImageURL() {
            return imageURL;
        }

        public void setImageURL(String imageURL) {
            this.imageURL = imageURL;
        }

        public String getImageURI() {
            return imageURI;
        }

        public void setImageURI(String imageURI) {
            this.imageURI = imageURI;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public void addPictureContentValues(ContentValues contentValues) {
            contentValues.put(YahooNewsFeedContract.NewsEntry.COLUMN_NAME_IMAGE_URL, getImageURL());
            contentValues.put(YahooNewsFeedContract.NewsEntry.COLUMN_NAME_IMAGE_SD_URI, getImageURI());
            contentValues.put(YahooNewsFeedContract.NewsEntry.COLUMN_NAME_IMAGE_TYPE, getImageType());
            contentValues.put(YahooNewsFeedContract.NewsEntry.COLUMN_NAME_IMAGE_WIDTH, getWidth());
            contentValues.put(YahooNewsFeedContract.NewsEntry.COLUMN_NAME_IMAGE_HEIGHT, getHeight());
        }

        @Override
        public String toString() {
            return "ImageData{" +
                    "imageType='" + imageType + '\'' +
                    ", imageURL='" + imageURL + '\'' +
                    ", imageURI='" + imageURI + '\'' +
                    ", height=" + height +
                    ", width=" + width +
                    '}';
        }
    }

}
