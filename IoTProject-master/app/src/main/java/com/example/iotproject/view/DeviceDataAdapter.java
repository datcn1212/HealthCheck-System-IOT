package com.example.iotproject.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.iotproject.R;
import com.example.iotproject.model.Device;
import com.example.iotproject.model.DeviceData;

public class DeviceDataAdapter extends ArrayAdapter<String> {
    Context context;
    DeviceInfoView activity;
    public DeviceDataAdapter(@NonNull Context context, int resource, String[] temp, DeviceInfoView activity) {
        super(context, resource, temp);
        this.context = context;
        this.activity = activity;
        System.out.println(activity.getDevice().getDatas().size());
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View row = inflater.inflate(R.layout.custom_device_data_item, null);

        TextView heartRate = (TextView) row.findViewById(R.id.heartRate);
        TextView temperature = (TextView) row.findViewById(R.id.bodyTemperature);
        TextView systolic = (TextView) row.findViewById(R.id.systolic);
        TextView diastolic = (TextView) row.findViewById(R.id.diastolic);
        TextView create = (TextView) row.findViewById(R.id.create);

        DeviceData data = activity.getDevice().getDatas().get(position);
        heartRate.setText(String.valueOf(data.getHeartRate()));
        temperature.setText(String.valueOf(data.getBodyTemperature()));
        systolic.setText(String.valueOf(data.getSystolic()));
        diastolic.setText(String.valueOf(data.getDiastolic()));
        create.setText(data.getCreate());

        System.out.println("Created data");

        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activity.getDevice().getDatas().get(position).getWarning() != null) {
                    Toast.makeText(activity, activity.getDevice().getDatas().get(position).getWarning(), Toast.LENGTH_LONG).show();
                }
            }
        });
        return (row);


    }
}
