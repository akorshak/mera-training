package com.example.ibulatov.emailcheckingservice.provider;

import android.content.ContentValues;
import android.database.Cursor;

import java.text.SimpleDateFormat;
import java.util.Date;

public class EmailMessage {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy ' at ' HH:mm:ss");

    private String mSender;
    private String mSubject;
    private String mText;
    private Date mReceivedDate;

    public EmailMessage() {
        mSender = "";
        mSubject = "";
        mText = "";
        mReceivedDate = new Date();
    }

    public EmailMessage(String sender, String subject, String text, Date receivedDate) {
        this.mSender = sender;
        this.mSubject = subject;
        this.mText = text;
        this.mReceivedDate = receivedDate;
    }

    public static EmailMessage fromCursor(Cursor cursor) {
        String sender, subject, text;
        Date receivedDate;

        try {
            sender = cursor.getString(cursor.getColumnIndexOrThrow(EmailServiceContract.EmailEntry.COLUMN_NAME_EMAILS_SENDER));
            receivedDate = DATE_FORMAT.parse(cursor.getString(cursor.getColumnIndexOrThrow(EmailServiceContract.EmailEntry.COLUMN_NAME_EMAILS_RECEIVED_DATE)));
            subject = cursor.getString(cursor.getColumnIndexOrThrow(EmailServiceContract.EmailEntry.COLUMN_NAME_EMAILS_SUBJECT));
            text = cursor.getString(cursor.getColumnIndexOrThrow(EmailServiceContract.EmailEntry.COLUMN_NAME_EMAILS_TEXT));
        } catch (Exception e) {
            return null;
        }

        return new EmailMessage(sender, subject, text, receivedDate);
    }

    public ContentValues createContentValues() {
        ContentValues cv = new ContentValues();
        cv.put(EmailServiceContract.EmailEntry.COLUMN_NAME_EMAIL_UID, this.hashCode());
        cv.put(EmailServiceContract.EmailEntry.COLUMN_NAME_EMAILS_SENDER, mSender);
        cv.put(EmailServiceContract.EmailEntry.COLUMN_NAME_EMAILS_TEXT, mText);
        cv.put(EmailServiceContract.EmailEntry.COLUMN_NAME_EMAILS_RECEIVED_DATE, DATE_FORMAT.format(mReceivedDate));
        cv.put(EmailServiceContract.EmailEntry.COLUMN_NAME_EMAILS_SUBJECT, mSubject);
        return cv;
    }

    public void setReceivedDate(Date receivedDate) {
        this.mReceivedDate = receivedDate;
    }

    public void setText(String text) {
        this.mText = text;
    }

    public void setSubject(String subject) {
        this.mSubject = subject;
    }

    public void setSender(String sender) {
        this.mSender = sender;
    }

    public Date getReceivedDate() {
        return mReceivedDate;
    }

    public String getText() {
        return mText;
    }

    public String getSubject() {
        return mSubject;
    }

    public String getSender() {
        return mSender;
    }

    public String getReceivedDateAsString() {
        return DATE_FORMAT.format(mReceivedDate);
    }

    @Override
    public int hashCode() {

        final int prime = 31;
        int result = 1;

        result = result * prime + mSender.hashCode();
        result = result * prime + mSubject.hashCode();
        result = result * prime + mText.hashCode();
        result = result * prime + mReceivedDate.hashCode();

        return result;
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof EmailMessage) {
            return o.hashCode() == this.hashCode();
        }
        return false;
    }

    @Override
    public String toString() {
        return this.hashCode() + "";
    }

}
