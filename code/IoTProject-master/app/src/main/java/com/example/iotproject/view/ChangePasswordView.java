package com.example.iotproject.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.iotproject.R;
import com.example.iotproject.api.ChangePassword;

public class ChangePasswordView extends AppCompatActivity {
    EditText oldPassword, newPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password_view);

        oldPassword = (EditText) findViewById(R.id.oldPassword);
        newPassword = (EditText) findViewById(R.id.newPassword);

        ChangePasswordView self = this;

        findViewById(R.id.changePasswordBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ChangePassword(self).execute(oldPassword.getText().toString(), newPassword.getText().toString());
            }
        });
    }
}