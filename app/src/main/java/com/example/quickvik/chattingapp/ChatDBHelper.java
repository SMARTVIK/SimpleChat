package com.example.quickvik.chattingapp;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Created by quickvik on 8/21/2015.
 */
public class ChatDBHelper {
    private static final java.lang.String QUERY_CREATE = buildCreateQuery();
    private static final String TABLE_NAME = "chat_messages";
    private static final String TAG = ChatDBHelper.class.getSimpleName();

    private static ChatDBHelper instance;

    private static final String COL_ROLE = "role";
    private static final String COL_TIMESTAMP = "timestamp";
    private static final String COL_MESSAGE = "message";
    private SQLiteDatabase database;

    private ChatDBHelper(SQLiteDatabase sqLiteDatabase) {
        this.database = sqLiteDatabase;
        executeCreateTableQuery();
    }

    public static ChatDBHelper init(SQLiteDatabase sqLiteDatabase) {
        if (instance == null) {
            instance = new ChatDBHelper(sqLiteDatabase);
        }
        return instance;
    }

    public static ChatDBHelper getInstance() {
        if (instance == null) {
            throw new IllegalStateException("instance not initialized yet");
        }
        return instance;
    }

    //building query
    private static String buildCreateQuery() {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE IF NOT EXISTS ").append(TABLE_NAME).append("(");
        sb.append(COL_TIMESTAMP).append(" ").append("TEXT PRIMARY KEY,");
        sb.append(COL_ROLE).append(" ").append("TEXT,");
        sb.append(COL_MESSAGE).append(" ").append("TEXT");
        sb.append(")");
        return sb.toString();
    }

    public void load() {
        //getting results in order of timstamp

        Cursor cursor = database.query(TABLE_NAME, null, null, null, null, null, "timestamp");

        Log.e("load", "loading started");
        if (!cursor.moveToFirst()) {
            return;//if cursor has no results then return
        }

        ArrayList<Messages> messagesArrayList = new ArrayList<>();
        do {
            Messages messages = new Messages(cursor.getString(cursor.getColumnIndex(COL_MESSAGE)), cursor.getString(cursor.getColumnIndex(COL_ROLE)), cursor.getString(cursor.getColumnIndex(COL_TIMESTAMP)));
            messagesArrayList.add(messages);
        } while (cursor.moveToNext());

        // setting the results to data controller

        DataController.getInstance().setMessages(messagesArrayList);
    }

//inserting records to db

    public boolean insert(Messages messages) {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO ").append(TABLE_NAME).append("(");
        sb.append(COL_TIMESTAMP).append(",");
        sb.append(COL_ROLE).append(",");
        sb.append(COL_MESSAGE);
        sb.append(") ");
        sb.append("VALUES").append("(");
        sb.append("\"").append(messages.getTimestamp()).append("\"").append(",");
        sb.append("\"").append(messages.getRole()).append("\"").append(",");
        sb.append("\"").append(messages.getMessage()).append("\"");
        sb.append(")");
        String query = sb.toString();
        try {
            database.execSQL(query);
            return true;
        } catch (Exception e) {
            Log.d(TAG, "update() ERROR: " + e);
        }
        return false;
    }

    private void executeCreateTableQuery() {
        database.execSQL(QUERY_CREATE);
    }
}
