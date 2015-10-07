package tng.fedorov.resources;

import android.os.Bundle;
import android.support.v4.app.ListFragment;

/**
 * Created by fedorov on 07.10.2015.
 */
public class MainFragment extends ListFragment {

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        String[] colornames = getResources().getStringArray(R.array.colornames);

        setListAdapter(new ColorArrayAdapter(getActivity(),
                android.R.layout.simple_list_item_1, colornames));
    }
}
