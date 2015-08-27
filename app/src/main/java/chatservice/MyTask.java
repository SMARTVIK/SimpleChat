package chatservice;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.quickvik.chattingapp.DataController;
import com.example.quickvik.chattingapp.Listener;
import com.example.quickvik.chattingapp.Messages;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.X509TrustManager;

public class MyTask extends AsyncTask<Void, Void, JSONObject> {
    Context context;
    ProgressDialog progressDialog;
    Listener listener;
    JSONArray jsonArray;

    private static final int _4KB = 4 * 1024;
    private final String WEB_URL = "https://demo7677878.mockable.io/getmessages";

    public MyTask(Context context, ProgressDialog progressDialog, Listener listener) {
        this.context = context;
        this.progressDialog = progressDialog;
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog.setTitle("loading....");
        progressDialog.show();
    }

    @Override
    protected void onPostExecute(JSONObject jsonArray) {
        super.onPostExecute(jsonArray);
        if (progressDialog != null && jsonArray != null) {
            progressDialog.cancel();
            try {
                JSONArray jsonArray1 = jsonArray.getJSONArray("messages");
                setDataForList(jsonArray1);
                listener.onSuccess(jsonArray1);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            listener.onError("something is wrong");
        }
    }

    @Override
    protected JSONObject doInBackground(Void... params) {
        HttpURLConnection conn = null;
        JSONArray jsonArray = null;
        JSONObject jsonObject = null;
        String response;
        try {
            trustEveryone();
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
                        jsonObject = new JSONObject(response);
                        jsonArray = jsonObject.getJSONArray("messages");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }


    private void trustEveryone() {
        try {
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, new X509TrustManager[]{new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] chain,
                                               String authType) throws CertificateException {
                }

                public void checkServerTrusted(X509Certificate[] chain,
                                               String authType) throws CertificateException {
                }

                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            }}, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(
                    context.getSocketFactory());
        } catch (Exception e) { // should never happen
            e.printStackTrace();
        }
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


}