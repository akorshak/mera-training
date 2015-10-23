package tng.fedorov.valcurs;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DataDownloader{

    public String downloadData(String urlRequest) throws IOException {
        URL url = new URL(urlRequest);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        if (urlConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
            return null;
        }

        try {
            StringBuilder sb = new StringBuilder();
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(urlConnection.getInputStream(), "windows-1251"));

            String nextLine;
            while ((nextLine = reader.readLine()) != null) {
                sb.append(nextLine);
            }
            reader.close();
            return sb.toString();

        } finally {
            urlConnection.disconnect();

            Log.d("log", "urlConnection.disconnect");
        }
    }

}

