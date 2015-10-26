package tng.fedorov.emailservice;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainFragment extends Fragment {

    private View mView;
    private EditText mEditTextServer;
    private EditText mEditTextPort;
    private EditText mEditTextLogin;
    private EditText mEditTextPassword;
    private EditText mEditTextInterval;
    private Button mButtonStartService;
    private Button mButtonStopService;
    private PendingIntent mPendingIntent;
    private AlarmManager mAlarmManager;
    private SharedPreferences mSharedPreferences;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_main, container, false);
        mEditTextServer = (EditText) mView.findViewById(R.id.editTextServer);
        mEditTextPort = (EditText) mView.findViewById(R.id.editTextPort);
        mEditTextLogin = (EditText) mView.findViewById(R.id.editTextLogin);
        mEditTextPassword = (EditText) mView.findViewById(R.id.editTextPassword);
        mEditTextInterval = (EditText) mView.findViewById(R.id.editTextInterval);
        mButtonStartService = (Button) mView.findViewById(R.id.buttonStartService);
        mButtonStopService = (Button) mView.findViewById(R.id.buttonStopService);
        return mView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAlarmManager = (AlarmManager) getActivity().
                getSystemService(Context.ALARM_SERVICE);
        mSharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        loadPreferences();

        mButtonStartService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int interval = Integer.valueOf(mEditTextInterval.getText().toString());
                Intent intent = new Intent(getActivity(), MyIntentService.class);
                intent.putExtra("server", mEditTextServer.getText().toString());
                intent.putExtra("port", mEditTextPort.getText().toString());
                intent.putExtra("login", mEditTextLogin.getText().toString());
                intent.putExtra("password", mEditTextPassword.getText().toString());
                mPendingIntent = PendingIntent.getService(getActivity(), 0, intent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
                mAlarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME, 0,
                        AlarmManager.INTERVAL_HOUR / interval, mPendingIntent);
                Toast.makeText(getActivity(), "Started", Toast.LENGTH_SHORT).show();
            }
        });

        mButtonStopService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MyIntentService.class);
                intent.putExtra("server", mEditTextServer.getText().toString());
                intent.putExtra("port", mEditTextPort.getText().toString());
                intent.putExtra("login", mEditTextLogin.getText().toString());
                intent.putExtra("password", mEditTextPassword.getText().toString());
                mPendingIntent = PendingIntent.getService(getActivity(), 0, intent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
                mAlarmManager.cancel(mPendingIntent);
                Toast.makeText(getActivity(), "Stopped", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onPause() {
        savePreferences();
        super.onPause();
    }

    private void savePreferences() {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString("server", mEditTextServer.getText().toString());
        editor.putString("port", mEditTextPort.getText().toString());
        editor.putString("login", mEditTextLogin.getText().toString());
        editor.putString("interval", mEditTextInterval.getText().toString());
        editor.apply();
    }

    private void loadPreferences() {
        mEditTextServer.setText(mSharedPreferences.getString("server",
                getResources().getString(R.string.server_example)));
        mEditTextPort.setText(mSharedPreferences.getString("port",
                getResources().getString(R.string.port_example)));
        mEditTextLogin.setText(mSharedPreferences.getString("login",
                getResources().getString(R.string.login_example)));
        mEditTextInterval.setText(mSharedPreferences.getString("interval",
                getResources().getString(R.string.interval_example)));
    }
}
