package com.example.iotproject.view;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.iotproject.R;
import com.example.iotproject.api.GetUserInfo;
import com.example.iotproject.model.Device;
import com.example.iotproject.model.User;

import java.util.ArrayList;
import java.util.List;

public class MainView extends ListActivity {

    private ArrayList<String> names;
    private ArrayList<String> ids;
    private ArrayList<String> createTimes;

    private Button addDeviceBtn;

    private static MainView shared;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_view);

        addDeviceBtn = (Button) findViewById(R.id.addDeviceBtn);
        Activity self = this;
        addDeviceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Button add device touched");
                Intent intent = new Intent(self, AddDeviceDialog.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                self.getApplicationContext().startActivity(intent);
            }
        });

        findViewById(R.id.userSetting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(self, UpdateUserInfoView.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                self.startActivity(intent);
            }
        });

        findViewById(R.id.changePassword).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(self, ChangePasswordView.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                self.startActivity(intent);
            }
        });

        findViewById(R.id.logoutBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(self, LoginView.class);
                User.USER_TOKEN = "";
                User.getInstance().reset();
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                self.startActivity(intent);
            }
        });

        new GetUserInfo(this).execute();
    }

    public void initData() {
        List<Device> deviceList = User.getInstance().getDevicesList();

        names = new ArrayList<>();
        ids = new ArrayList<>();
        createTimes = new ArrayList<>();

        System.out.println("Device list length " + deviceList.size());

        for (Device device : deviceList) {
            names.add(device.getName());
            ids.add(device.getId());
            createTimes.add(String.valueOf(device.getCreateTime()));
        }
        CustomAdapter customAdapter = new CustomAdapter(this, R.layout.custom_list_item, names.toArray(new String[0]), ids.toArray(new String[0]), createTimes.toArray(new String[0]), this);
        setListAdapter(customAdapter);
    }

    public static MainView getInstance() {
        if (shared == null) {
            shared = new MainView();
        }
        return shared;
    }
}