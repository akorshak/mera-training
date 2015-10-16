package tng.fedorov.simpledialer;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MainFragment extends Fragment {

    private EditText mEditText;
    private Button mButtonSave;
    private Button mButtonAction;
    private static final String FILE = "source";

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_main, container, false);

        mEditText = (EditText) view.findViewById(R.id.editText);
        mButtonSave = (Button) view.findViewById(R.id.buttonSave);
        mButtonAction = (Button) view.findViewById(R.id.buttonAction);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final PackageManager pm = getActivity().getPackageManager();

        mButtonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    write();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Error on save operation", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mButtonAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = null;
                try {
                    text = read();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Error on read operation", Toast.LENGTH_SHORT).show();
                }

                if (text == null || text.isEmpty()) {
                    Toast.makeText(getActivity(), "No data", Toast.LENGTH_SHORT).show();
                } else if (Patterns.EMAIL_ADDRESS.matcher(text.trim()).matches()) {
                    Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + text.trim()));
                    startActivity(intent);
                } else if (Patterns.PHONE.matcher(text.trim()).matches()) {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + text.trim()));
                    if (pm.resolveActivity(intent,PackageManager.MATCH_ALL)==null) {
                        Toast.makeText(getActivity(), "Data not supported", Toast.LENGTH_SHORT).show();
                    } else {
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(getActivity(), "No valid data", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void write() throws IOException {

        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(
                    getActivity().openFileOutput(FILE, Context.MODE_PRIVATE)));
            writer.write(mEditText.getText().toString());
        } finally {
            if (writer!=null) {
                writer.close();
            }
        }
    }

    private String read() throws IOException {
        String result;
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(getActivity().openFileInput(FILE)));
            result = reader.readLine();
        } finally {
            if (reader!=null) {
                reader.close();
            }
        }
        return result;
    }

}
