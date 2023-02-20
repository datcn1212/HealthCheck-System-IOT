package com.example.iotproject.api;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.Gravity;
import android.widget.Toast;

import com.example.iotproject.model.User;
import com.example.iotproject.view.MainView;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class Login extends AsyncTask<String, String, String> {
    Activity activity;
    ProgressDialog progressDialog;
    public Login(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        // display a progress dialog to show the user what is happening
        progressDialog = new ProgressDialog(activity);
        progressDialog.show();


    }

    @Override
    protected String doInBackground(String... strings) {

        // Fetch data from the API in the background.

        String result = "";
        URL url;
        HttpsURLConnection urlConnection = null;

        try {

            url = new URL("https://iot-health.onrender.com/user/login");

            // Params list
            Map<String, Object> params = new LinkedHashMap<>();
            params.put("username", strings[0]);
            params.put("password", strings[1]);

            // Convert map to String
            StringBuilder postData = new StringBuilder();
            for (Map.Entry<String, Object> param : params.entrySet()) {
                if (postData.length() != 0) postData.append('&');
                postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                postData.append('=');
                postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));

            }

            // Convert string to byte
            byte[] postDataBytes = postData.toString().getBytes("UTF-8");

            // Set property for connection
            urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            urlConnection.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
            urlConnection.setDoOutput(true);
            urlConnection.getOutputStream().write(postDataBytes);

            //
            InputStream in = urlConnection.getInputStream();
            InputStreamReader isw = new InputStreamReader(in);


            int data = isw.read();

            while (data != -1) {
                result += (char) data;
                data = isw.read();
            }

            System.out.println("Received data is" + result);
            User.USER_TOKEN = result;
            result = "+" + result;

            urlConnection.disconnect();
        } catch (Exception e) {
//            e.printStackTrace();
            System.out.println("Error is " + e.getMessage());
            try {
                InputStream errorStream = urlConnection.getErrorStream();
                InputStreamReader errorStreamReader = new InputStreamReader(errorStream);
                int data = errorStreamReader.read();
                while (data != -1) {
                    result += (char) data;
                    data = errorStreamReader.read();
                }
            } catch (IOException io) {
                io.printStackTrace();
            }

        }

        return result;
    }

    @Override
    protected void onPostExecute(String s) {
        System.out.println("Result is " + s);
        progressDialog.hide();
        if (s.charAt(0) == '+') {
            Toast.makeText(activity, "Login success", Toast.LENGTH_SHORT).show();
            User.USER_TOKEN = s.substring(2, s.length() - 1);
            onSuccess();
        } else {
            Toast.makeText(activity, s, Toast.LENGTH_LONG).show();
        }
        // show results
    }

    protected void onSuccess() {
        Intent intent = new Intent(activity, MainView.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(intent);
    }
}
