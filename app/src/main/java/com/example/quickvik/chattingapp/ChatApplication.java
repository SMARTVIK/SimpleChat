package com.example.quickvik.chattingapp;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import chatservice.MyChatService;
import chatservice.MyTask;

/**
 * Created by quickvik on 8/21/2015.
 */
public class ChatApplication extends Application {

    private static ChatApplication instance;
    private boolean closed;

    public static ChatApplication getInstance() {
        return instance;
    }

    private Map<Class<? extends BaseUIListener>, Collection<? extends BaseUIListener>> uiListeners;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        closed = false;
        uiListeners = new HashMap<>();

      //  BackgroundExecutor.getInstance().execute(new RateDoctorRequester());


        //startService(new Intent(getApplicationContext(), MyChatService.class));

        /*
        if (!isServiceRunning(MyChatService.class)) {

        } else {
            stopService(new Intent(getApplicationContext(), MyChatService.class));
            startService(new Intent(getApplicationContext(), MyChatService.class));
        }*/


        MessageDBHelper dbOpenHelper = new MessageDBHelper(
                getApplicationContext(), "chatapp");
        dbOpenHelper.createDatabase();
        SQLiteDatabase database = dbOpenHelper.openDB();
        Singleton.getInstance().setDbhelper(database);
        //loadSavedData();

    }

    /**
     * Register new listener. * * Should be called from {@link Activity#onResume()}. * * @param cls * @param listener
     */
    public <T extends BaseUIListener> void addUIListener(Class<T> cls, T listener) {
        getOrCreateUIListeners(cls).add(listener);
    }

    /**
     * Unregister listener. * * Should be called from {@link Activity#onPause()}. * * @param cls * @param listener
     */
    public <T extends BaseUIListener> void removeUIListener(Class<T> cls, T listener) {
        getOrCreateUIListeners(cls).remove(listener);
    }

    /**
     * @param cls * Requested class of listeners. * @return List of registered UI listeners.
     */

    public <T extends BaseUIListener> Collection<T> getUIListener(Class<T> cls) {
        if (closed) return Collections.emptyList();
        return Collections.unmodifiableCollection(getOrCreateUIListeners(cls));
    }

    @SuppressWarnings("unchecked")
    private <T extends BaseUIListener> Collection<T> getOrCreateUIListeners(Class<T> cls) {
        Collection<T> collection = (Collection<T>) uiListeners.get(cls);
        if (collection == null) {
            collection = new ArrayList<T>();
            uiListeners.put(cls, collection);
        }
        return collection;
    }

    private boolean isServiceRunning(Class<?> cls) {
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);

        String myService = cls.getName();

        for (ActivityManager.RunningServiceInfo serviceInfo : activityManager.getRunningServices(Integer.MAX_VALUE)) {

            String serviceName = serviceInfo.service.getClassName();
            if (serviceName.contains(myService)) {
                return true;
            }
            if (serviceName.equals(myService)) {
                return true;

            }
        }

        return false;
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
