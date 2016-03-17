package com.example.ibulatov.emailcheckingservice.provider;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import com.example.ibulatov.emailcheckingservice.provider.EmailServiceContract.EmailEntry;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class EmailsProviderUtils {


    public static void insertEmailsList(ContentResolver resolver, List<EmailMessage> emailMessageList) {

        ContentValues[] cvs = new ContentValues[emailMessageList.size()];

        for(int i = 0; i < emailMessageList.size(); i++) {
            cvs[i] = emailMessageList.get(i).createContentValues();
        }

        resolver.bulkInsert(EmailsProvider.EMAIL_CONTENT_URI, cvs);
    }

    public static List<EmailMessage> loadEmailMessages(ContentResolver resolver) {

        List<EmailMessage> emailMessageList = new ArrayList<>();

        Cursor c = resolver.query(EmailsProvider.EMAIL_CONTENT_URI, EmailServiceContract.EmailEntry.ALL, null, null, null);
        if(c != null && c.getCount() > 0) {
            while (c.moveToNext()) {
                EmailMessage message = EmailMessage.fromCursor(c);
                if(message != null) {
                    emailMessageList.add(message);
                }
            }
            c.close();
        }
        return emailMessageList;
    }

    public static Cursor selectAll(ContentResolver resolver) {
        return resolver.query(EmailsProvider.EMAIL_CONTENT_URI, EmailEntry.ALL, null, null, null);
    }



}
