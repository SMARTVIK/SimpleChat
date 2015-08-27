package com.example.quickvik.chattingapp;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.security.Security;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class RateDoctorRequester implements Runnable {
    public static final String TAG = RateDoctorRequester.class.getName();

    public RateDoctorRequester() {
        // params initializations
    }


    private void setDataForList(JSONArray jsonArray) {
        ArrayList<Messages> messages = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Messages messages1 = new Messages(jsonObject.getString("message"), jsonObject.getString("role"), jsonObject.getString("timestamp"));
                DataController.getInstance().insertMessage(messages1);
                messages.add(messages1);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        DataController.getInstance().setMessages(messages);
    }

    private static final int _4KB = 4 * 1024;
    private final String WEB_URL = "https://demo7677878.mockable.io/getmessages";

    public JSONArray getDataFromServer() {
        HttpURLConnection conn = null;
        JSONArray jsonArray = null;
        String response;
        try {
            URL url = new URL(WEB_URL);
            if (checkHTTPS(WEB_URL)) {
                conn = (HttpsURLConnection) url.openConnection();
            } else {
                conn = (HttpURLConnection) url.openConnection();
            }
            conn.connect();
            int statusCode = conn.getResponseCode();
            switch (statusCode) {
                case 200:
                    response = new String(readFullyBytes(conn.getInputStream(), 2 * _4KB));
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        jsonArray = jsonObject.getJSONArray("messages");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonArray;
    }

    private byte[] readFullyBytes(InputStream is, int blockSize) {
        byte[] bytes = null;
        if (is != null) {
            try {
                int readed = 0;
                byte[] buffer = new byte[blockSize];
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                while ((readed = is.read(buffer)) >= 0) {
                    bos.write(buffer, 0, readed);
                }
                bos.flush();
                bytes = bos.toByteArray();
            } catch (IOException e) {

            }
        }
        return bytes;
    }

    private boolean checkHTTPS(String web_url) {
        if (web_url.contains("https")) {
            return true;
        }
        return false;
    }


    {
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {

                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                        //No need to implement.
                    }

                    public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                        //No need to implement.
                    }
                }
        };
        // Install the all-trusting trust manager
        try {
            HttpsURLConnection.setDefaultHostnameVerifier(new NullHostNameVerifier());
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    class NullHostNameVerifier implements HostnameVerifier {

        @Override
        public boolean verify(String hostname, SSLSession session) {
            Log.i("RestUtilImpl", "Approving certificate for " + hostname);
            return true;
        }

    }

    @Override
    public void run() {

        JSONArray jsonArray = getDataFromServer();
        setDataForList(jsonArray);

        for (Listener listener : ChatApplication.getInstance().getUIListener(Listener.class)) {
            if (jsonArray != null) {
                listener.onSuccess(jsonArray);
            } else {
                listener.onError("jsonArray is null");
            }
        }
    }
}