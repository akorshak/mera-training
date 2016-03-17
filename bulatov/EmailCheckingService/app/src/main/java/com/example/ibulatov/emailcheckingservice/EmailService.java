package com.example.ibulatov.emailcheckingservice;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.example.ibulatov.emailcheckingservice.provider.EmailMessage;
import com.example.ibulatov.emailcheckingservice.provider.EmailServiceContract;
import com.example.ibulatov.emailcheckingservice.provider.EmailsProvider;
import com.example.ibulatov.emailcheckingservice.provider.EmailsProviderUtils;

import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class EmailService extends Service {

    public static final String LOG_TAG = "EmailService";

    private String mServer;
    private String mPort;
    private String mPassword;
    private String mLogin;

    private TimerTask mEmailCheckTask;
    private Timer mTimer;

    private int mCheckPeriod;

    private EmailReader mEmailReader;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d(LOG_TAG, "onStartCommand");

        mServer = intent.getStringExtra(ExtrasConstants.EMAIL_SERVER_EXTRA);
        mPort = intent.getStringExtra(ExtrasConstants.EMAIL_PORT_EXTRA);
        mPassword = intent.getStringExtra(ExtrasConstants.EMAIL_PASSWORD_EXTRA);
        mLogin = intent.getStringExtra(ExtrasConstants.EMAIL_LOGIN_EXTRA);
        mCheckPeriod = intent.getIntExtra(ExtrasConstants.EMAIL_CHECK_PERIOND, 0);

        if(TextUtils.isEmpty(mServer)
                || TextUtils.isEmpty(mPort)
                || TextUtils.isEmpty(mPassword)
                || TextUtils.isEmpty(mLogin)
                || mCheckPeriod == 0) {

            stopSelf();

        } else {
            mEmailReader = new EmailReader(mServer, mPort, mLogin, mPassword);
            mTimer = new Timer();

            startScheduledTask();
        }

        return START_NOT_STICKY;
    }

    private void startScheduledTask() {
        if (mEmailCheckTask != null) mEmailCheckTask.cancel();
        if (mCheckPeriod > 0) {
            mEmailCheckTask = new TimerTask() {
                @Override
                public void run() {

                    Log.d(LOG_TAG, "executing scheduled task");

                    List<EmailMessage> messageList = mEmailReader.readMailMessages();

                    if(messageList != null) {

                        List<EmailMessage> messageDbList = EmailsProviderUtils.loadEmailMessages(getContentResolver());

                        int counter = 0;
                        for(EmailMessage message : messageList) {
                            if(messageDbList.contains(message)) {
                                counter++;
                            }
                        }

                        if(!(counter == messageDbList.size() && messageDbList.size() == messageList.size())) {
                            getContentResolver().delete(EmailsProvider.EMAIL_CONTENT_URI, null, null);
                            EmailsProviderUtils.insertEmailsList(getContentResolver(), messageList);

                            if(counter < messageList.size() && messageDbList.size() > 0){
                                startActivity(new Intent(EmailService.this, EmailListActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                            }

                        }
                    }

                }
            };
            mTimer.schedule(mEmailCheckTask, 0, mCheckPeriod * 1000);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mTimer != null) mTimer.cancel();
        if (mEmailCheckTask != null) mEmailCheckTask.cancel();

        Log.d(LOG_TAG, "onDestroy");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
