package com.example.ibulatov.emailcheckingservice.provider;

import android.provider.BaseColumns;

public final class EmailServiceContract {
    public EmailServiceContract() {}

    public static abstract class EmailEntry implements BaseColumns {

        public static final String TABLE_NAME = "emails_table";
        public static final String COLUMN_NAME_EMAIL_UID = "uid";
        public static final String COLUMN_NAME_EMAILS_SENDER = "sender";
        public static final String COLUMN_NAME_EMAILS_TEXT = "text";
        public static final String COLUMN_NAME_EMAILS_SUBJECT = "subject";
        public static final String COLUMN_NAME_EMAILS_RECEIVED_DATE = "received_date";

        public static final String[] ALL = new String[] {
                _ID,
                COLUMN_NAME_EMAIL_UID,
                COLUMN_NAME_EMAILS_SENDER,
                COLUMN_NAME_EMAILS_TEXT,
                COLUMN_NAME_EMAILS_SUBJECT,
                COLUMN_NAME_EMAILS_RECEIVED_DATE
        };

    }
}