package com.example.quickvik.chattingapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.database.DataSetObserver;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ssl.HttpsURLConnection;

import chatservice.MyTask;

public class ChatActivity extends Activity implements Listener {

    private ChatAdapter chatArrayAdapter;
    private ListView listView;
    private EditText chatText;
    private Button buttonSend;
    private boolean side = false;
    private ArrayList<Messages> messages = null;

    ProgressDialog pDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_chat);
        buttonSend = (Button) findViewById(R.id.buttonSend);
        listView = (ListView) findViewById(R.id.listView1);

       /*loading data from database*/

        if (DataController.getInstance().getMessages() != null) {
            messages = DataController.getInstance().getMessages();
            settingAdapter();
        }
     /* if database is empty then hit the webservice*/
        else {
            pDialog = new ProgressDialog(this);
            pDialog.show();
            new MyTask(this, new ProgressDialog(this), this).execute();
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    public boolean onSuccess(JSONArray jsonArray) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ChatActivity.this.pDialog.dismiss();
                settingAdapter();
            }
        });

        return false;
    }

    @Override
    public boolean onError(String meg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ChatActivity.this.pDialog.dismiss();
                Toast.makeText(ChatActivity.this, "something is wrong", Toast.LENGTH_SHORT).show();
            }
        });
        return false;
    }
    private boolean sendChatMessage() {
        String role = side ? "sender" : "receiver";
        Messages messages = new Messages(chatText.getText().toString(), role, Long.toString(System.currentTimeMillis()));
        chatArrayAdapter.add(messages);
        DataController.getInstance().insertMessage(messages);
        chatText.setText("");
        side = !side;
        return true;
    }

    private void settingAdapter() {
        messages = DataController.getInstance().getMessages();

        if (messages == null) {
            Toast.makeText(this, "messages are null", Toast.LENGTH_SHORT).show();
            return;
        }
        chatArrayAdapter = new ChatAdapter(getApplicationContext(), messages);
        listView.setAdapter(chatArrayAdapter);
        chatText = (EditText) findViewById(R.id.chatText);
        chatText.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    return sendChatMessage();
                }
                return false;
            }
        });
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                sendChatMessage();
            }
        });
        listView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        listView.setAdapter(chatArrayAdapter);
        chatArrayAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                listView.setSelection(chatArrayAdapter.getCount() - 1);
            }
        });
    }


}


