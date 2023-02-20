package com.example.iotproject.view;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.iotproject.R;
import com.example.iotproject.api.Login;
import com.example.iotproject.model.User;

public class LoginView extends AppCompatActivity {

    EditText username, password;
    Button loginBtn;
    TextView registerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = (EditText) findViewById(R.id.oldPassword);
        password = (EditText) findViewById(R.id.newPassword);
        loginBtn = (Button) findViewById(R.id.changePasswordBtn);
        registerBtn = (TextView) findViewById(R.id.signUpBtn);
        User.USER_TOKEN = null;

        initListener();

    }

    private void initListener() {
        Activity self = this;
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Login btn touched");
                String usernameInput = username.getText().toString();
                String passwordInput = password.getText().toString();
                Login loginTask = new Login(self);
                loginTask.execute(usernameInput, passwordInput);
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Register btn touched");
                Intent registerOpen = new Intent(LoginView.this, RegisterView.class);
                LoginView.this.startActivity(registerOpen);

            }
        });

    }

    public void changeToMainActivity() {
        Intent intent = new Intent(LoginView.this, MainView.class);
        LoginView.this.startActivity(intent);
    }

}