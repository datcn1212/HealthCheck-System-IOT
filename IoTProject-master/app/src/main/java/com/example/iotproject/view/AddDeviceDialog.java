package com.example.iotproject.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.iotproject.R;
import com.example.iotproject.api.CreateDevice;

public class AddDeviceDialog extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device_dialog);
        AddDeviceDialog self = this;
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                self.finish();
            }
        });

        findViewById(R.id.changePasswordBtn).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String id = ((EditText) findViewById(R.id.newPassword)).getText().toString();
                String name = ((EditText) findViewById(R.id.oldPassword)).getText().toString();
                new CreateDevice(self).execute(id, name);
            }
        });
    }

}