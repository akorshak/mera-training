package tng.fedorov.emailservice;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.widget.Toast;

import javax.mail.Message;

public class MyIntentService extends IntentService {

    public MyIntentService() {
        super("MyIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            String server = intent.getStringExtra("server");
            String port = intent.getStringExtra("port");
            String login = intent.getStringExtra("login");
            String password = intent.getStringExtra("password");
            MailReader mailReader = new MailReader(server, port, login, password);
            try {
                final Message[] messages = mailReader.readMail();
                if (messages.length > 0) {
                    startActivity(new Intent(this, MainActivity.class).
                            setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    Handler handler = new Handler(getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "You have " + messages.length + " unread messages.",
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
