package com.example.ibulatov.emailcheckingservice;

import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.ibulatov.emailcheckingservice.provider.EmailServiceContract;
import com.example.ibulatov.emailcheckingservice.provider.EmailServiceContract.*;
import com.example.ibulatov.emailcheckingservice.provider.EmailsProvider;

public class EmailListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int EMAIL_LOADER_ID = 0;
    private static final String LOG_TAG = "EmailListActivity";

    private ListView mEmailsListView;
    private CursorAdapter mEmailsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.email_list_activity);

        mEmailsListView = (ListView) findViewById(R.id.emails_listView);
        mEmailsAdapter = new EmailsAdapter(this, null);
        mEmailsListView.setAdapter(mEmailsAdapter);

        getLoaderManager().initLoader(EMAIL_LOADER_ID, null, this);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        Log.d(LOG_TAG, "onCreateLoader");

        if(id == EMAIL_LOADER_ID) {
            return new CursorLoader(this,
                    EmailsProvider.EMAIL_CONTENT_URI,
                    EmailServiceContract.EmailEntry.ALL,
                    null,
                    null,
                    null);
        }

        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.d(LOG_TAG, "onLoadFinished");
        mEmailsAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.d(LOG_TAG, "onLoaderReset");
        mEmailsAdapter.swapCursor(null);
    }

    private static class EmailsAdapter extends CursorAdapter {

        private LayoutInflater mLayoutInflater;
        private Context mContext;

        public EmailsAdapter(Context context, Cursor c) {
            super(context, c, 0);

            this.mContext = context;
            this.mLayoutInflater = LayoutInflater.from(context);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            return mLayoutInflater.inflate(R.layout.email_message_row, parent, false);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {

            String sender = cursor.getString(cursor.getColumnIndexOrThrow(EmailEntry.COLUMN_NAME_EMAILS_SENDER));
            String date = cursor.getString(cursor.getColumnIndexOrThrow(EmailEntry.COLUMN_NAME_EMAILS_RECEIVED_DATE));
            String subject = cursor.getString(cursor.getColumnIndexOrThrow(EmailEntry.COLUMN_NAME_EMAILS_SUBJECT));

            TextView senderTextView = (TextView) view.findViewById(R.id.sender_value_textView);
            TextView dateTextView = (TextView) view.findViewById(R.id.date_value_textView);
            TextView subjectTextView = (TextView) view.findViewById(R.id.subject_value_textView);

            senderTextView.setText(sender);
            dateTextView.setText(date);
            subjectTextView.setText(subject);

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(this, EmailService.class));
    }
}
