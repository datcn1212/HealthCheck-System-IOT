package com.example.iotproject.api;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.iotproject.model.User;
import com.example.iotproject.view.DeviceInfoView;
import com.example.iotproject.view.MainView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class UpdateDeviceInfo extends AsyncTask<String, String, String> {

    DeviceInfoView activity;
    ProgressDialog progressDialog;
    int responseCode;

    public UpdateDeviceInfo(DeviceInfoView activity) {
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

            url = new URL("https://iot-health.onrender.com/heartSensor/" + strings[0]);

            // Params list
            Map<String, Object> params = new LinkedHashMap<>();
            params.put("name", strings[1]);
            params.put("info", strings[2]);

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

            InputStreamReader isw =  new InputStreamReader(in);
            responseCode = urlConnection.getResponseCode();
            int data = isw.read();

            while (data != -1) {
                result += (char) data;
                data = isw.read();
            }

            System.out.println("Received data is" + " " + urlConnection.getRequestMethod());
            urlConnection.disconnect();

            return result;

        } catch (Exception e) {
            try {
                if (urlConnection.getResponseCode() >= 400) {
                    responseCode = urlConnection.getResponseCode();
                    result = "Thiết bị không tìm thấy";
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }

        return result;
    }

    @Override
    protected void onPostExecute(String s) {
        // show results
        System.out.println("Result is " + s + responseCode);
        // show results
        if (responseCode < 400) {
            progressDialog.hide();
            Toast.makeText(activity, "Cập nhật thông tin thiết bị thành công", Toast.LENGTH_LONG).show();
            try {
                activity.getDevice().getData(new JSONObject(s));
                activity.initData();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {
            Toast.makeText(activity, s, Toast.LENGTH_LONG).show();
            progressDialog.hide();
        }
    }
}
