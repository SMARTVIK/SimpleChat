package com.example.quickvik.chattingapp;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by quickvik on 8/21/2015.
 */
public class ChatApplication extends Application {

    private static ChatApplication instance;
    private boolean closed;

    public static ChatApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        closed = false;

        MessageDBHelper dbOpenHelper = new MessageDBHelper(
                getApplicationContext(), "chatapp");
        dbOpenHelper.createDatabase();
        SQLiteDatabase database = dbOpenHelper.openDB();
        Singleton.getInstance().setDbhelper(database);
        loadSavedData();

    }

    private void loadSavedData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                DataController.getInstance().loadData();
            }
        }).start();
    }


}
