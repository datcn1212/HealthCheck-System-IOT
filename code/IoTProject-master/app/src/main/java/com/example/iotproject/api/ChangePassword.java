package com.example.iotproject.api;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.iotproject.model.User;
import com.example.iotproject.view.ChangePasswordView;
import com.example.iotproject.view.MainView;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class ChangePassword extends AsyncTask<String, String, String> {
    ChangePasswordView activity;
    ProgressDialog progressDialog;
    int responseCode;
    public ChangePassword(ChangePasswordView activity) {
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

            url = new URL("https://iot-health.onrender.com/user/changePassword");

            // Params list
            Map<String, Object> params = new LinkedHashMap<>();
            params.put("oldPassword", strings[0]);
            params.put("newPassword", strings[1]);

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
                result = String.valueOf(urlConnection.getResponseCode());
                responseCode = urlConnection.getResponseCode();
                if (result.equals("408"))
                    result = "Mật khẩu cũ không đúng";
            } catch (IOException exception) {
                exception.printStackTrace();
            }
            e.printStackTrace();
        }

        return result;
    }

    @Override
    protected void onPostExecute(String s) {
        System.out.println("Result is " + s);
        // show results
        if (responseCode < 400) {
            progressDialog.hide();
            Toast.makeText(activity, "Change password success", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(activity, MainView.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            activity.startActivity(intent);
        } else {
            progressDialog.hide();
            Toast.makeText(activity, s, Toast.LENGTH_LONG).show();
        }
    }
}
