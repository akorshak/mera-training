package com.example.ibulatov.networktest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class PageFetcher {

    private static final int DEFAULT_CONNECT_TIMEOUT_MILLS = 10000;
    private static final int DEFAULT_READ_TIMEOUT_MILLS = 10000;
    private static final String GET_REQUEST_METHOD = "GET";
    private static final String DEFAULT_USER_AGENT = "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10_6_7; en-US) AppleWebKit/534.16 (KHTML, like Gecko) Chrome/10.0.648.205 Safari/534.16";
    private static final String DEFAULT_ENCODING = "UTF-8";

    public String fetch(String url) throws IOException {

        HttpURLConnection httpcon = null;
        InputStream is = null;

        try {

            URL link = new URL(url);
            httpcon = (HttpURLConnection) link.openConnection();

            httpcon.setRequestProperty("User-Agent", DEFAULT_USER_AGENT);
            httpcon.setConnectTimeout(DEFAULT_CONNECT_TIMEOUT_MILLS);
            httpcon.setReadTimeout(DEFAULT_READ_TIMEOUT_MILLS);
            httpcon.setRequestMethod(GET_REQUEST_METHOD);

            int responseCode = httpcon.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {

                is = httpcon.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, DEFAULT_ENCODING));

                StringBuilder sb = new StringBuilder();
                String line;
                while((line = reader.readLine())!= null){
                    sb.append(line);
                }

                return sb.toString();

            } else {
                throw new IOException ("bad response code");
            }

        } finally {
            if(is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                }
            }

            if(httpcon != null) {
                httpcon.disconnect();
            }
        }
    }
}
