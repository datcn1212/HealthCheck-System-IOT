package com.example.iotproject.view;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.iotproject.R;
import com.example.iotproject.api.RegisterUser;

public class RegisterView extends AppCompatActivity {

    Button registerBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_view);

        registerBtn = (Button) findViewById(R.id.changePasswordBtn);
        Activity self = this;
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username, password, name, weight, height, age;

                username = ((TextView) findViewById(R.id.oldPassword)).getText().toString();
                password = ((TextView) findViewById(R.id.newPassword)).getText().toString();
                name = ((TextView) findViewById(R.id.nameUpdate)).getText().toString();
                weight = ((TextView) findViewById(R.id.weightUpdate)).getText().toString();
                height = ((TextView) findViewById(R.id.heightUpdate)).getText().toString();
                age = ((TextView) findViewById(R.id.ageUpdate)).getText().toString();

                new RegisterUser(self).execute(username, password, name, age, weight, height);
            }
        });
    }
}