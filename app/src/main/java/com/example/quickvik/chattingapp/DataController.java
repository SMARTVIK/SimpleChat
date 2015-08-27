package com.example.quickvik.chattingapp;

import java.util.ArrayList;

/**
 * Created by quickvik on 8/21/2015.
 */
public class DataController {

    private static  DataController instance= null;

    public ArrayList<Messages> getMessages() {
        return this.messages;
    }

    public void setMessages(ArrayList<Messages> messages) {
        this.messages = messages;
    }

    private ArrayList<Messages> messages;

    public static DataController getInstance() {
        if(instance==null)
        {
             instance =new DataController();
        }
        return instance;
    }

    private DataController() {
        ChatDBHelper.init(Singleton.getInstance().getMessageDBHelper());
    }

    public boolean insertMessage(Messages messages) {
        return ChatDBHelper.getInstance().insert(messages);
    }
    public void loadData() {
        ChatDBHelper.getInstance().load();
    }
}
