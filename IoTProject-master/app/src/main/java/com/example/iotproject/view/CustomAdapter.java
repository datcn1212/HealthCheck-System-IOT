package com.example.iotproject.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.iotproject.R;

public class CustomAdapter extends ArrayAdapter<String> {
    Context context;
    String[] names;
    String[] ids;
    String[] createTime;
    MainView activity;
    public CustomAdapter(@NonNull Context context, int resource, String[] names, String[] subjects, String[] times, MainView activity) {
        super(context, resource, names);
        this.context = context;
        this.names = names;
        this.ids = subjects;
        this.createTime = times;
        this.activity = activity;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View row = inflater.inflate(R.layout.custom_list_item, null);
        TextView name = (TextView) row.findViewById(R.id.name);
        TextView subject = (TextView) row.findViewById(R.id.subject);
        TextView time = (TextView) row.findViewById(R.id.time);

        name.setText(names[position]);
        subject.setText(ids[position]);
        time.setText(createTime[position]);

        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Item " + position + "clicked");
                Intent intent = new Intent(activity, DeviceInfoView.class);
                intent.putExtra("position", position);
                activity.startActivity(intent);

            }
        });
        return (row);


    }

}

