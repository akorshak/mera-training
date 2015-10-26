package tng.fedorov.emailservice;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.search.FlagTerm;

public class MailReader extends Authenticator {
    private String mServer;
    private String mPort;
    private String mLogin;
    private String mPassword;

    public MailReader(String server, String port, String login, String password) {
        this.mServer = server;
        this.mPort = port;
        this.mLogin = login;
        this.mPassword = password;
    }

    public PasswordAuthentication getPasswordAuthentication()
    {
        return new PasswordAuthentication(mLogin, mPassword);
    }

    public synchronized Message[] readMail() throws Exception {
        Properties props = new Properties();
        props.setProperty("mail.store.protocol", "imaps");
        props.setProperty("mail.imaps.host", mServer);
        props.setProperty("mail.imaps.port", mPort);

        Session session = Session.getInstance(props,this);
        Store store = session.getStore("imaps");
        store.connect(mLogin, mPassword);

        Folder folder = store.getFolder("INBOX");
        folder.open(Folder.READ_ONLY);

        Message[] messages = folder.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));
        folder.close(false);
        store.close();

        return messages;
    }
}
