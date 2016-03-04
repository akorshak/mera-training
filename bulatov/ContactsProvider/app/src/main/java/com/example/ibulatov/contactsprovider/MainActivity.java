package com.example.ibulatov.contactsprovider;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.Contacts;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.SimpleCursorTreeAdapter;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements ExpandableListView.OnChildClickListener {

    private static final String LOG_TAG = "MainActivity";

    private Cursor cursor;
    private ExpandableListView listContainer;

    @TargetApi(23)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listContainer = (ExpandableListView) findViewById(R.id.elvMain);

        if((Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)) {
            if (checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED)
                requestPermissions(new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS}, 1);
            else
                init();

        } else {
            init();
        }

    }


    public void init() {
        cursor = ContactsProviderUtils.queryContactsData(getContentResolver());
        listContainer.setAdapter(createAdapter());
        listContainer.setOnChildClickListener(this);
    }

    public SimpleCursorTreeAdapter createAdapter() {

        String[] groupFrom = { Contacts.DISPLAY_NAME };
        int[] groupTo = { android.R.id.text1 };

        String[] childFrom = { Email.ADDRESS };
        int[] childTo = { android.R.id.text1 };

        return new MyAdapter(this, cursor,
                android.R.layout.simple_expandable_list_item_1, groupFrom,
                groupTo, android.R.layout.simple_list_item_1, childFrom,
                childTo);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            init();
        }  else {
            Toast.makeText(MainActivity.this, "Access denied", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        Cursor cursor = (Cursor) parent.getExpandableListAdapter().getChild(groupPosition, childPosition);

        String email = cursor.getString(cursor.getColumnIndex(Email.ADDRESS));
        String contactId = cursor.getString(cursor.getColumnIndex(Email.CONTACT_ID));

        DialogFragment dialog = EmailEditDialogFragment.newInstance(contactId, email);
        dialog.show(getSupportFragmentManager(), "emailEditFrag");
        return false;
    }


    // FIXME: SimpleCursorTreeAdapter works incorrectly due to unclosed cursors after adapter recreation
    // SimpleCursorTreeAdapter should be replaced with CursorLoader.

    class MyAdapter extends SimpleCursorTreeAdapter {

        public MyAdapter(Context context, Cursor cursor, int groupLayout,
                         String[] groupFrom, int[] groupTo, int childLayout,
                         String[] childFrom, int[] childTo) {
            super(context, cursor, groupLayout, groupFrom, groupTo,
                    childLayout, childFrom, childTo);
        }

        protected Cursor getChildrenCursor(Cursor groupCursor) {
            int idColumn = groupCursor.getColumnIndex(Contacts._ID);
            return ContactsProviderUtils.queryEmailsData(getContentResolver(), groupCursor.getString(idColumn));
        }

    }


}
