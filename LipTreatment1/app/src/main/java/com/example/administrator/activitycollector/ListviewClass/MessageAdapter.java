package com.example.administrator.activitycollector.ListviewClass;

import android.content.Context;
import android.os.*;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.administrator.activitycollector.R;

import java.util.List;

/**
 * Created by Scong on 2017/12/8.
 */

public class MessageAdapter extends ArrayAdapter<Message> {

    private int resourceId;

    public MessageAdapter(@NonNull Context context, int textViewResourceId, List<Message> objects) {
        super(context,textViewResourceId,objects);
        resourceId=textViewResourceId;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Message message=getItem(position);
        View view= LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        TextView messageTitle=(TextView) view.findViewById(R.id.message_title);
        messageTitle.setText(message.getTitle());
        return view;
    }
}













