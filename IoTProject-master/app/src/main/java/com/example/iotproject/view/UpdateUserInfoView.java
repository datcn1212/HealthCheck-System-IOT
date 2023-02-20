package com.example.iotproject.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;

import com.example.iotproject.R;
import com.example.iotproject.api.UpdateUserInfo;
import com.example.iotproject.model.User;

public class UpdateUserInfoView extends AppCompatActivity {

    EditText username, name, height, age, weight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user_info_view);

        username = (EditText) findViewById(R.id.oldPassword);
        username.setInputType(InputType.TYPE_NULL);
        name = (EditText) findViewById(R.id.nameUpdate);
        height = (EditText) findViewById(R.id.heightUpdate);
        age = (EditText) findViewById(R.id.ageUpdate);
        weight = (EditText) findViewById(R.id.weightUpdate);

        initData();
        UpdateUserInfoView self = this;
        findViewById(R.id.changePasswordBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new UpdateUserInfo(self).execute(name.getText().toString(), age.getText().toString(), weight.getText().toString(), height.getText().toString());
            }
        });
    }

    public void initData() {
        username.setText(User.getInstance().getUsername());
        name.setText(User.getInstance().getName());
        height.setText(String.valueOf(User.getInstance().getHeight()));
        age.setText(String.valueOf(User.getInstance().getAge()));
        weight.setText(String.valueOf(User.getInstance().getWeight()));

    }
}