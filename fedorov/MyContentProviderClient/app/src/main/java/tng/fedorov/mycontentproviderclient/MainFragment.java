package tng.fedorov.mycontentproviderclient;

import android.app.Fragment;
import android.app.ListFragment;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

/**
 * Created by fedorov on 15.10.2015.
 */
public class MainFragment extends Fragment {

    private static final Uri URI = Uri.parse("content://tng.fedorov.provider/items");
    private static final String NAME = "name";
    private ListView mListView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment, container, false);
        mListView = (ListView) view.findViewById(R.id.listView);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Cursor cursor = getActivity().getContentResolver().query(URI, null, null, null, null);
        String from[] = { "_id","name" };
        int to[] = { android.R.id.text1,android.R.id.text2 };
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(getActivity(),
                android.R.layout.simple_list_item_2, cursor, from, to);
        mListView.setAdapter(adapter);
    }

    public void onClickInsert(View view) {
        ContentValues cv = new ContentValues();
        cv.put(NAME, "name 10");
        Uri uri = getActivity().getContentResolver().insert(URI, cv);
    }

    public void onClickUpdate(View view) {
        ContentValues cv = new ContentValues();
        cv.put(NAME, "name 100");
        Uri uri = ContentUris.withAppendedId(URI, 2);
        int rows = getActivity().getContentResolver().update(uri, cv, null, null);
    }

    public void onClickDelete(View view) {
        Uri uri = ContentUris.withAppendedId(URI, 3);
        int rows = getActivity().getContentResolver().delete(uri, null, null);
    }

}
