package tng.fedorov.valcurs;

import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ValItemFragment extends ListFragment implements LoaderManager.LoaderCallbacks<ArrayList<ValItem>>{

    Bundle mBundle;
    ListView mListView;
    String mDate;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Log.d("log", "onActivityCreated--ValItemFragment");

        mBundle = this.getArguments();
        mDate = mBundle.getString("date");
        mListView = getListView();

        if (savedInstanceState != null && mDate.equals(savedInstanceState.getString("date"))) {
            getActivity().getLoaderManager().initLoader(1, mBundle, this);
        } else {
            setListShown(false);
            getActivity().getLoaderManager().restartLoader(1, mBundle, this);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("date", mDate);
        super.onSaveInstanceState(outState);
    }

    @Override
    public Loader<ArrayList<ValItem>> onCreateLoader(int id, Bundle args) {
        Log.d("log", "onCreateLoader");

        ValLoader loader = new ValLoader(getActivity(), args);
        loader.forceLoad();
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<ValItem>> loader, ArrayList<ValItem> data) {
        if (data == null || data.isEmpty()) {
            Toast.makeText(getActivity(), "No data!", Toast.LENGTH_SHORT).show();
        } else {
            mListView.setAdapter(new ValAdapter(getActivity(), R.layout.row, data));
            setListShown(true);
        }
        Log.d("log", "onLoadFinished");
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<ValItem>> loader) {
        Log.d("log", "onLoaderReset");
    }
}
