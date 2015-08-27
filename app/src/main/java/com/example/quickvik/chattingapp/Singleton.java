package com.example.quickvik.chattingapp;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by quickvik on 8/21/2015.
 */
public class Singleton {
    private static Singleton instance = null;
    private SQLiteDatabase database;

    public static Singleton getInstance() {
        if (instance == null) {
            instance = new Singleton();
        }
        return instance;
    }

    public void setDbhelper(SQLiteDatabase database) {
        this.database = database;
    }

    public SQLiteDatabase getMessageDBHelper() {
        return database;
    }
}
