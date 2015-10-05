package com.example.quickvik.chattingapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by quickvik on 8/21/2015.
 */
public class MessageDBHelper extends SQLiteOpenHelper {
    private static final String TAG = MessageDBHelper.class.getSimpleName();
    private String DATABASE_NAME;
    private static final int DATABASE_VERSION = 1;
    //private final String DB_PATH;
    private Context context;
    private SQLiteDatabase mDatabase;



    public MessageDBHelper(Context context, String name) {
        super(context, name, null, DATABASE_VERSION);
        this.context = context;

/*
        if (android.os.Build.VERSION.SDK_INT >= 17) {
            DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
        } else {
            DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
        }*/
        DATABASE_NAME = name;

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

      mDatabase =db;
    }

    private void copyDatabase() throws IOException {
       /* InputStream myInput = context.getAssets().open(DATABASE_NAME);
        String outFileName = DB_PATH + DATABASE_NAME;
        OutputStream myOutput = new FileOutputStream(outFileName);
        byte[] buffer = new byte[1024];
        int length;

        while ((length = myInput.read(buffer)) > 0)
            myOutput.write(buffer, 0, length);

        myOutput.flush();
        myOutput.close();
        myInput.close();
   */ }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


    }

    public void createDatabase() {
        if (!isDatabaseExist()) {
            this.openDatabase();
        }
    }

    public SQLiteDatabase openDatabase() {
        if (mDatabase != null) {
            if (!mDatabase.isOpen()) {
                mDatabase = this.getWritableDatabase();
            }
        } else {
            mDatabase = this.getWritableDatabase();
        }
        return mDatabase;
    }

    private boolean isDatabaseExist() {
        try {
            this.openDatabase();
            if (mDatabase != null && mDatabase.isOpen()) {
                this.mDatabase.close();
            }
        } catch (SQLiteException e) {
            // database doesn't exist yet.
            Log.e(TAG, "Error");
            e.printStackTrace();
        }
        return mDatabase != null ? true : false;
    }


    public SQLiteDatabase openDB() {
        if (!mDatabase.isOpen()) {
            mDatabase = this.getWritableDatabase();
            // this.getWritableDatabase();
        }
        return mDatabase;
    }

}
