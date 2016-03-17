package com.example.ibulatov.emailcheckingservice;

import android.util.Log;

import com.example.ibulatov.emailcheckingservice.provider.EmailMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.search.FlagTerm;

public class EmailReader extends Authenticator {

    public static final String LOG_TAG = "EmailReader";

    private String mServer;
    private String mPort;
    private String mLogin;
    private String mPassword;

    private Properties props;

    public EmailReader(String server, String port, String login, String password) {
        this.mServer = server;
        this.mPort = port;
        this.mLogin = login;
        this.mPassword = password;

        props = new Properties();
        props.setProperty("mail.store.protocol", "imaps");
        props.setProperty("mail.imaps.host", mServer);
        props.setProperty("mail.imaps.port", mPort);
    }

    public List<EmailMessage> readMailMessages() {
        Log.d(LOG_TAG, "reading mail messages");

        Folder folder = null;
        Store store = null;
        List<EmailMessage> emailMessageList = null;

        try {
            Session session = Session.getInstance(props, this);
            store = session.getStore("imaps");
            store.connect(mLogin, mPassword);

            folder = store.getFolder("inbox");
            folder.open(Folder.READ_ONLY);

            Message[] messages = folder.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));

            emailMessageList = new ArrayList<>();

            for(int i = 0; i < messages.length; i++) {

                EmailMessage message = new EmailMessage();
                message.setSender(messages[i].getFrom()[0].toString());
                message.setSubject(messages[i].getSubject());
                message.setReceivedDate(messages[i].getReceivedDate());
//                message.setText(messages[i].getContent().toString());

                emailMessageList.add(message);
            }

        } catch (NoSuchProviderException e) {
            Log.e(LOG_TAG, e.getMessage());
            emailMessageList = null;
        } catch (MessagingException e) {
            Log.e(LOG_TAG, e.getMessage());
            emailMessageList = null;
        } finally {
            if(folder != null )
                try {
                    folder.close(false);
                } catch (MessagingException e) {
                    Log.e(LOG_TAG, e.getMessage());
                }
            if(store != null) {
                try {
                    store.close();
                } catch (MessagingException e) {
                    Log.e(LOG_TAG, e.getMessage());
                }
            }
        }

        return emailMessageList;
    }



}
