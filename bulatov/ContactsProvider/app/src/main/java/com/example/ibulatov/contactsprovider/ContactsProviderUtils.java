package com.example.ibulatov.contactsprovider;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.Contacts;

public class ContactsProviderUtils {

    private static final String[] EMAIL_PROJECTION = new String[]{ Email._ID, Email.CONTACT_ID, Email.ADDRESS };
    private static final String[] CONTACTS_PROJECTION  = new String[] { Contacts._ID, Contacts.DISPLAY_NAME };

    private static final String EMAIL_MIME_TYPE = "vnd.android.cursor.item/email_v2";

    public static Cursor queryEmailsData(ContentResolver resolver, String contactId) {
        return resolver.query(
                Email.CONTENT_URI,
                EMAIL_PROJECTION,
                Email.CONTACT_ID + " = ?",
                new String[] { contactId },
                null);
    }

    public static Cursor queryContactsData(ContentResolver resolver) {
        return resolver.query(
                Contacts.CONTENT_URI,
                CONTACTS_PROJECTION,
                null,
                null,
                null);
    }

    public static int updateContactsEmail(ContentResolver resolver, String contactId, String newEmail) {

        ContentValues cv = new ContentValues();
        cv.put(Email.ADDRESS, newEmail);

        return resolver.update(ContactsContract.Data.CONTENT_URI, cv, Email.CONTACT_ID + " = ? AND " + Email.MIMETYPE + " = ?", new String[]{contactId, EMAIL_MIME_TYPE});

//        ArrayList<ContentProviderOperation> ops = new ArrayList<>();
//        ops.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
//                .withSelection(Email.CONTACT_ID + " = ? AND " + Email.MIMETYPE + " = ?", new String[]{contactId, "vnd.android.cursor.item/email_v2"})
//                .withValue(Email.ADDRESS, newEmail)
//                .build());
//        resolver.applyBatch(ContactsContract.AUTHORITY, ops);

    }




}
