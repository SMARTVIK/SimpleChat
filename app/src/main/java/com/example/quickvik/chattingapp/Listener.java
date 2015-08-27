package com.example.quickvik.chattingapp;

import org.json.JSONArray;

/**
 * Created by quickvik on 8/25/2015.
 */
public interface Listener extends BaseUIListener {
    public boolean onSuccess(JSONArray jsonArray);
    public boolean onError(String meg);
}
