package com.example.iotproject.view;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.iotproject.R;
import com.example.iotproject.api.DeleteDevice;
import com.example.iotproject.api.GetDeviceData;
import com.example.iotproject.api.UpdateDeviceInfo;
import com.example.iotproject.model.Device;
import com.example.iotproject.model.User;

public class DeviceInfoView extends ListActivity {
    Device device;
    public TextView deviceId;
    public EditText deviceName, deviceInfo;

    public Device getDevice() {
        return device;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_info_view);
        int position = getIntent().getIntExtra("position", 0);
        System.out.println(position);

        device = User.getInstance().getDevicesList().get(position);

        deviceId = (TextView) findViewById(R.id.deviceId);
        deviceName = (EditText) findViewById(R.id.deviceName);
        deviceInfo = (EditText) findViewById(R.id.deviceInfo);

        initData();

        DeviceInfoView self = this;
        findViewById(R.id.deleteDeviceBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DeleteDevice(self).execute(device.get_id());
            }
        });

        findViewById(R.id.updateDeviceBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new UpdateDeviceInfo(self).execute(device.get_id(), deviceName.getText().toString(), deviceInfo.getText().toString());
            }
        });

        new GetDeviceData(this).execute(device.getId());

    }

    public void initData() {
        deviceId.setText(device.getId());
        deviceName.setText(device.getName());
        deviceInfo.setText(device.getInfo());
    }

    public void initListView() {
        DeviceDataAdapter customAdapter = new DeviceDataAdapter(this, R.layout.custom_device_data_item, new String[device.getDatas().size()], this);
        setListAdapter(customAdapter);
        System.out.println(customAdapter.getCount());
    }
}

