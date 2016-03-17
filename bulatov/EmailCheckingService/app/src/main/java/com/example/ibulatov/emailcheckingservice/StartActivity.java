package com.example.ibulatov.emailcheckingservice;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ibulatov.emailcheckingservice.provider.EmailsProvider;

public class StartActivity extends AppCompatActivity {

    public static final String LOG_TAG = "StartActivity";

    private EditText mLogin;
    private EditText mPassword;
    private EditText mServer;
    private EditText mPort;
    private EditText mPeriod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_activity);
        
        mLogin = (EditText) findViewById(R.id.login_editText);
        mPassword = (EditText) findViewById(R.id.password_editText);
        mServer = (EditText) findViewById(R.id.server_editText);
        mPort = (EditText) findViewById(R.id.port_editText);
        mPeriod = (EditText) findViewById(R.id.period_editText);

    }

    public static class MyAsyncQueryHandler extends AsyncQueryHandler {
        public MyAsyncQueryHandler(ContentResolver cr) {
            super(cr);
        }
    }

    public void startEmailCheckingService(View view) {

        new MyAsyncQueryHandler(getContentResolver()).startDelete(-1, null, EmailsProvider.EMAIL_CONTENT_URI, null, null);

        String login = mLogin.getText().toString();
        String password = mPassword.getText().toString();
        String server = mServer.getText().toString();
        String port = mPort.getText().toString();
        String periodStr = mPeriod.getText().toString();

        if(TextUtils.isEmpty(login)
                || TextUtils.isEmpty(password)
                || TextUtils.isEmpty(server)
                || TextUtils.isEmpty(port)
                || TextUtils.isEmpty(periodStr)) {

            Toast.makeText(this, "Blank field", Toast.LENGTH_SHORT).show();

        } else {

            int period;
            try {
                period = Integer.parseInt(mPeriod.getText().toString());
            } catch (NumberFormatException e) {
                Log.e(LOG_TAG, e.getMessage());
                Toast.makeText(this, "Wrong period type", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(this, EmailService.class);
            intent.putExtra(ExtrasConstants.EMAIL_LOGIN_EXTRA, login);
            intent.putExtra(ExtrasConstants.EMAIL_PASSWORD_EXTRA, password);
            intent.putExtra(ExtrasConstants.EMAIL_SERVER_EXTRA, server);
            intent.putExtra(ExtrasConstants.EMAIL_PORT_EXTRA, port);
            intent.putExtra(ExtrasConstants.EMAIL_CHECK_PERIOND, period);

            startService(intent);
            startActivity(new Intent(this, EmailListActivity.class));

        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "onDestroy");
    }
}
