package tng.fedorov.simpletexteditor;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;


public class MainFragment extends Fragment {

    private SharedPreferences mSharedPreferences;
    private EditText mEditText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        mEditText = (EditText) view.findViewById(R.id.editText);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        loadText();
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onPause() {
        saveText();
        super.onPause();
    }

    private void saveText() {
        mSharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString("text", mEditText.getText().toString());
        editor.apply();
    }

    private void loadText() {
        mSharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        String text = mSharedPreferences.getString("text", "");
        mEditText.setText(text);
    }
}
