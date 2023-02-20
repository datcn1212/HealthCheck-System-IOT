package com.example.iotproject.api;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.iotproject.model.User;
import com.example.iotproject.view.MainView;
import com.example.iotproject.view.UpdateUserInfoView;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class UpdateUserInfo extends AsyncTask<String, String, String> {

    UpdateUserInfoView activity;
    ProgressDialog progressDialog;
    int responseCode;
    public UpdateUserInfo(UpdateUserInfoView activity) {
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

            url = new URL("https://iot-health.onrender.com/user/information");

            // Params list
            Map<String, Object> params = new LinkedHashMap<>();
            params.put("username", User.getInstance().getUsername());
            params.put("name", strings[0]);
            params.put("age", strings[1]);
            params.put("weight", strings[2]);
            params.put("height", strings[3]);

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
            urlConnection.setRequestMethod("PUT");
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            urlConnection.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
            urlConnection.setRequestProperty("token", User.USER_TOKEN);
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

            System.out.println("Received data is" + " " + urlConnection.getRequestMethod());
            responseCode = urlConnection.getResponseCode();
            urlConnection.disconnect();

            return result;

        } catch (Exception e) {
            try {
                responseCode = urlConnection.getResponseCode();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        System.out.println("Result is " + s);
        if (responseCode < 400) {
            progressDialog.hide();
            Toast.makeText(activity, "Update success", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(activity, MainView.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            activity.startActivity(intent);
        } else {
            progressDialog.hide();
            Toast.makeText(activity, s, Toast.LENGTH_LONG).show();
        }
        // show results
    }
}