package tng.fedorov.valcurs;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainFragment extends Fragment {

    TextView mTvDate;
    Button mBtnLoad;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);
        mTvDate = (TextView) view.findViewById(R.id.textViewDate);
        mBtnLoad = (Button) view.findViewById(R.id.buttonLoad);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Log.d("log", "onActivityCreated--MainFragment");

        if (savedInstanceState == null || savedInstanceState.isEmpty()) {

            final String date = new SimpleDateFormat("dd.MM.yyyy").format(Calendar.getInstance().getTime());
            mTvDate.setText(date);
            showData();

            Log.d("log", "showData onActivityCreated");
        }

        mTvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerFragment().show(getActivity().getFragmentManager(), "datePicker");
            }
        });

        mBtnLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("log", "showData onClick");
                showData();
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("key", "value");
        super.onSaveInstanceState(outState);
    }

    private void showData() {
        String targetDate = mTvDate.getText().toString().replace(".", "/");
        String urlRequest = getActivity().getResources().getString(R.string.url_base) + targetDate;
        Bundle bundle = new Bundle();
        bundle.putString("url", urlRequest);
        bundle.putString("date", targetDate);
        ValItemFragment fragment = new ValItemFragment();
        fragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer, fragment);
        fragmentTransaction.commit();

        Log.d("log", "fragmentTransaction");
    }
}