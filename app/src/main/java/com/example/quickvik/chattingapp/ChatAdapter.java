package com.example.quickvik.chattingapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ChatAdapter extends BaseAdapter {

    private List<Messages> chatMessageList;
    private Context context;

    public void add(Messages object) {
        chatMessageList.add(object);
    }

    public ChatAdapter(Context context, List<Messages> chatMessageList) {
        this.chatMessageList = chatMessageList;
        this.context=context;
    }

    public int getCount() {
        return chatMessageList.size();
    }

    public Messages getItem(int index) {
        return chatMessageList.get(index);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder;

        if (row == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.activity_chat_singlemessage, parent, false);
            holder.singleMessageContainer = (LinearLayout) row.findViewById(R.id.singleMessageContainer);
            holder.chatText = (TextView) row.findViewById(R.id.singleMessage);
            row.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) row.getTag();
        }
        Messages chatMessageObj = getItem(position);
        holder.chatText.setText(chatMessageObj.getMessage());

        if (chatMessageObj.getRole().equals("sender")) {
            holder.chatText.setBackgroundResource(R.drawable.bubble_a);
            holder.singleMessageContainer.setGravity(Gravity.RIGHT);
        } else if (chatMessageObj.getRole().equals("receiver")) {
            holder.chatText.setBackgroundResource(R.drawable.bubble_b);
            holder.singleMessageContainer.setGravity(Gravity.LEFT);
        }

        return row;
    }

    public Bitmap decodeToBitmap(byte[] decodedByte) {
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    private class ViewHolder {
        TextView chatText;
        LinearLayout singleMessageContainer;
    }
}