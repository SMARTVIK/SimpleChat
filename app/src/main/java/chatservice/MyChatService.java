package chatservice;

import android.app.ProgressDialog;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

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
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by quickvik on 8/21/2015.
 */
public class MyChatService extends Service {

    private static final int _4KB = 4 * 1024;
    private final String WEB_URL = "https://demo7677878.mockable.io/getmessages";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);

/*

        new MyTask(this, new ProgressDialog(this), new Listener() {
            @Override
            public boolean onSuccess(JSONArray jsonArray) {
                if (jsonArray != null) {
                    return true;
                } else {
                    return false;
                }
            }


            @Override
            public boolean onError(String msg) {
                Log.d("Error msg", msg);
                return false;
            }

        }).execute();
*/


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

    private void setDataForList(JSONArray jsonArray) {
        ArrayList<Messages> messages = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Messages messages1 = new Messages(jsonObject.getString("message"), jsonObject.getString("role"), jsonObject.getString("timestamp"));
                DataController.getInstance().insertMessage(messages1);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


}
